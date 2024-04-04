package com.philips.itaap.utility.serivce;

import com.fasterxml.jackson.core.type.TypeReference;
import com.philips.itaap.ms.dev.base.exception.ServiceException;
import com.philips.itaap.utility.config.AzureProperties;
import com.philips.itaap.utility.constant.AzureConstants;
import com.philips.itaap.utility.dto.MicroservicesDTO;
import com.philips.itaap.utility.entity.AccProd;
import com.philips.itaap.utility.entity.Microservices;
import com.philips.itaap.utility.entity.NonProd;
import com.philips.itaap.utility.exception.AzureException;
import com.philips.itaap.utility.model.AzureResponse;

import com.philips.itaap.utility.model.deployment.BuildFolder;
import com.philips.itaap.utility.model.deployment.FolderDetails;
import com.philips.itaap.utility.model.deployment.Records;
import com.philips.itaap.utility.model.deployment.RunDetails;
import com.philips.itaap.utility.model.deployment.Record;
import com.philips.itaap.utility.repository.AccProdRepo;
import com.philips.itaap.utility.repository.DeploymentRepo;
import com.philips.itaap.utility.repository.MicroservicesRepo;
import com.philips.itaap.utility.repository.NonProdRepo;
import com.philips.itaap.utility.utils.Transformer;
import lombok.extern.slf4j.XSlf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@XSlf4j
@SuppressWarnings("CPD-START")
public class DeploymentService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private AzureProperties properties;

    public Mono<String> fetchDetails(String uri) {
        WebClient.ResponseSpec requestBodySpec = webClient
                .get()
                .uri(uri)
                .headers(getConsumerHeader())
                .retrieve();
        return getOnStatus(requestBodySpec).bodyToMono(String.class);
    }

    private Consumer<HttpHeaders> getConsumerHeader() {
        return httpHeaders -> httpHeaders.setBasicAuth(StringUtils.EMPTY, properties.getPat());
    }

    /**
     * This method is used to capture exception if status is 4xx/5xx exception is thrown else ResponseSpec is returned.
     *
     * @param responseSpec {@link WebClient.ResponseSpec}
     * @return {@link WebClient.ResponseSpec}
     */
    private WebClient.ResponseSpec getOnStatus(WebClient.ResponseSpec responseSpec) {
        return responseSpec.onStatus(HttpStatus::isError, response ->
                response.bodyToMono(AzureException.class).flatMap(error -> Mono.error(new ServiceException(response.statusCode(), error.getEventId(), error.getMessage()))));
    }

    public String createTreeStructure(String string) {
        AzureResponse<FolderDetails> response = Transformer.readValue(string, new TypeReference<>() {
        });
        BuildFolder root = BuildFolder.newChild("Root", "Root_folder", new ArrayList<>());
        response.getValue().forEach(folderDetails -> {
            BuildFolder node = addNodes(List.of(folderDetails.getFolder().substring(1).split("\\\\")), root);
            if (node != null) {
                root.getChildren().add(node);
            }
        });

        response.getValue().forEach(folderDetails -> {
            List<String> dirs = List.of(folderDetails.getFolder().substring(1).split("\\\\"));
            String currentParent = dirs.get(dirs.size() - 1);
            List<BuildFolder> folders = root.getChildren();
            for (String dir : dirs) {
                BuildFolder folder = findChild(dir, folders);
                if (folder != null) {
                    if (folder.getLabel().equals(currentParent)) {
                        folder.getChildren().add(BuildFolder.newChild(List.of(folderDetails.getFolder().substring(1).split("\\\\")), folderDetails.getName(), folderDetails.getId()));
                    } else {
                        folders = folder.getChildren();
                    }
                }
            }
        });
        return Transformer.getJSONString(root.getChildren().stream().distinct().sorted(Comparator.comparing(BuildFolder::getLabel)).collect(Collectors.toList()));
    }

    public BuildFolder addNodes(List<String> dirs, BuildFolder root) {
        BuildFolder node = null;
        List<BuildFolder> folders = root.getChildren();
        for (String dir : dirs) {
            BuildFolder folder = BuildFolder.newChild(dir, dir + "_Folder", new ArrayList<>());
            if (root.getChildren().stream().noneMatch(s -> s.getLabel().equals(dirs.get(0)))) {
                if (node != null) {
                    node.getChildren().add(folder);
                } else {
                    node = folder;
                }
            } else {
                BuildFolder result = findChild(dir, folders);
                if (result != null) {
                    folders = result.getChildren();
                } else {
                    folders.add(folder);
                    break;
                }
            }
        }
        return node;
    }

    private BuildFolder findChild(String dir, List<BuildFolder> list) {
        for (BuildFolder folder : list) {
            if (folder.getLabel().equals(dir)) {
                return folder;
            }
        }
        return null;
    }

    public Mono<List<RunDetails>> createTableStructure(String string) {
        AzureResponse<RunDetails> response = Transformer.readValue(string, new TypeReference<>() {
        });

        List<RunDetails> runDetailsList = new ArrayList<>();
        String url = "_build/results?buildId=";
        response.getValue().forEach(runDetails -> runDetailsList
                .add(new RunDetails(runDetails.getState(),
                        runDetails.getResult(),
                        runDetails.getId(),
                        runDetails.getName(),
                        runDetails.getUrl().split("_")[0] + url + runDetails.getId())));

        return Mono.just(runDetailsList);
    }

    public Mono<Map<Integer, List<Record>>> populateMsData(List<RunDetails> runDetailsList) {
        AtomicLong currentBuildNumberDev = new AtomicLong(0);
        AtomicLong currentBuildNumberTest = new AtomicLong(0);
        List<Record> recordDev = new ArrayList<>();
        List<Record> recordTest = new ArrayList<>();
        List<Record> recordAcc = new ArrayList<>();
        List<Record> recordProd = new ArrayList<>();

        Map<Integer, List<Record>> map = new HashMap<>();
        return Flux
                .fromIterable(runDetailsList)
                .flatMap(runDetails -> {
                    String uri = properties.getGetBuildTimeline().replace(AzureConstants.ORG_PLACEHOLDER, properties.getOrg())
                            .replace(AzureConstants.BUILD_ID_PLACEHOLDER, runDetails.getId().toString());
                    return fetchDetails(uri)
                            .map(timelines -> {
                                Records records = Transformer.readValue(timelines, Records.class);
                                records.getRecords().forEach(stage -> {
                                    stage.setBuildNumber(runDetails.getName());
                                    stage.setBuildID(runDetails.getId());
                                });
                                return records.getRecords()
                                        .stream()
                                        .filter(stage -> stage.getType().equals("Stage") && stage.getState().equalsIgnoreCase("completed"))
                                        .collect(Collectors.toList());
                            });
                })
                .collectList()
                .flatMap(runList -> {
                    runList.forEach(timeline -> timeline.forEach(stage -> {
                        if (stage.getBuildID() > currentBuildNumberDev.get()
                                && stage.getIdentifier().equalsIgnoreCase("dev")
                                && (stage.getResult().equalsIgnoreCase("succeeded")
                                || stage.getResult().equalsIgnoreCase("failed"))
                                && !stage.getName().toLowerCase().contains("valid")) {
                            recordDev.add(stage);
                            currentBuildNumberDev.set(stage.getBuildID());
                        }
                        if (stage.getBuildID() > currentBuildNumberTest.get()
                                && stage.getIdentifier().equalsIgnoreCase("test")
                                && (stage.getResult().equalsIgnoreCase("succeeded")
                                || stage.getResult().equalsIgnoreCase("failed"))
                                && !stage.getName().toLowerCase().contains("valid")) {
                            recordTest.add(stage);
                            currentBuildNumberTest.set(stage.getBuildID());
                        }
                        if (stage.getIdentifier().equalsIgnoreCase("acc")
                                && (stage.getResult().equalsIgnoreCase("succeeded")
                                || stage.getResult().equalsIgnoreCase("failed"))
                                && !stage.getName().toLowerCase().contains("valid")) {
                            recordAcc.add(stage);
                        }
                        if (stage.getIdentifier().equalsIgnoreCase("prod")
                                && (stage.getResult().equalsIgnoreCase("succeeded")
                                || stage.getResult().equalsIgnoreCase("failed"))
                                && !stage.getName().toLowerCase().contains("valid")) {
                            recordProd.add(stage);
                        }
                    }));
                    return Mono.just(runList);
                })
                .flatMap(o -> {
                    map.put(1, recordDev);
                    map.put(2, recordTest);
                    map.put(3, recordAcc);
                    map.put(4, recordProd);
                    return Mono.just(map);
                });
    }

    public String createTimeLineStructure(String string, String buildNumber) {
        Records records = Transformer.readValue(string, Records.class);
        List<Record> recordsList = records
                .getRecords()
                .stream()
                .filter(recordItem -> recordItem.getType().equals("Stage") && recordItem.getOrder() != 0)
                .sorted(Comparator.comparing(Record::getOrder))
                .collect(Collectors.toList());
        recordsList.forEach(stage -> {
            stage.setBuildNumber(buildNumber);
            if (stage.getResult() != null) {
                switch (stage.getResult()) {
                    case "pending":
                        stage.setIcon("pi pi-clock");
                        stage.setColor("#0078d4");
                        break;
                    case "succeeded":
                        stage.setIcon("pi pi-check");
                        stage.setColor("#55a362");
                        break;
                    case "running":
                    case "inProgress":
                        stage.setIcon("pi pi-sync");
                        stage.setColor("#0078d4");
                        break;
                    case "succeededWithIssues":
                        stage.setIcon("pi pi-info");
                        stage.setColor("#d67f3c");
                        break;
                    case "skipped":
                        stage.setIcon("pi pi-chevron-right");
                        stage.setColor("#212529");
                        break;
                    case "failed":
                        stage.setIcon("pi pi-times");
                        stage.setColor("#cd4a45");
                        break;
                    case "cancelled":
                    case "abandoned":
                        stage.setIcon("pi pi-ban");
                        stage.setColor("#212529");
                        break;
                    default:
                        stage.setIcon("pi pi-circle");
                        stage.setColor("#212529");
                        break;
                }
            } else if (stage.getState() != null && stage.getState().equals("pending")) {
                stage.setIcon("pi pi-clock");
                stage.setColor("#0078d4");
                stage.setResult(stage.getState());
            } else if (stage.getState() != null && stage.getState().equals("inProgress")) {
                stage.setIcon("pi pi-sync");
                stage.setColor("#0078d4");
                stage.setResult(stage.getState());
            }
        });
        return Transformer.getJSONString(recordsList);
    }


    public Mono<List<Map<String, String>>> replaceScriptsData(List<Map<String, String>> data, String cdName, String msName, String buildNumber, String platform) {
        data.forEach(map -> map.forEach((key, value) -> {
            value = value.replace("[[MICROSERVICE_CD_NAME]]", cdName);
            value = value.replace("[[MICROSERVICE_NAME]]", msName);
            value = value.replace("[[BUILD_NUMBER]]", buildNumber);
            value = value.replace("[[PLATFORM_NAME]]", platform);

            map.put(key, value);
        }));
        return Mono.just(data);
    }
}