package com.philips.itaap.utility.serivce;

import com.philips.itaap.utility.dto.DocumentsDTO;
import com.philips.itaap.utility.dto.MicroservicesDTO;
import com.philips.itaap.utility.entity.*;
import com.philips.itaap.utility.model.deployment.Record;
import com.philips.itaap.utility.repository.AccProdRepo;
import com.philips.itaap.utility.repository.DeploymentRepo;
import com.philips.itaap.utility.repository.MicroservicesRepo;
import com.philips.itaap.utility.repository.NonProdRepo;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@XSlf4j
public class DatabaseService {

    @Autowired
    private MsService msService;

    @Autowired
    private MicroservicesRepo microservicesRepo;

    @Autowired
    private NonProdRepo nonProdRepo;

    @Autowired
    private AccProdRepo accProdRepo;

    @Autowired
    private DeploymentRepo deploymentRepo;

    public Mono<Microservices> insertInDB(Map<Integer, List<Record>> map, Integer pipelineId, MicroservicesDTO dto) {
        String name = dto.getName().toLowerCase(Locale.ROOT).replace("-cd", "");
        dto.setName(name);
        dto.setPipelineID(pipelineId);

        Microservices microservice = msService.createOrUpdateMicroservice(dto);

        AtomicReference<NonProd> dev = new AtomicReference<>();
        AtomicReference<NonProd> test = new AtomicReference<>();

        Deployment deployment = Deployment
                .builder()
                .id(pipelineId)
//                .itd(dto.getItd())
//                .postmanCollection(dto.getPostmanCollection())
                .tgl(dto.getTgl())
                .bgl(dto.getBgl())
                .microserviceID(microservice.getId())
                .build();
        if (microservice.getDeployment() == null) {
            deployment = deploymentRepo.save(deployment);
            if (log.isInfoEnabled()) {
                log.info("CREATED : DEPLOYMENT_ROW -> [{}]", deployment.getId());
            }
        }

        Deployment finalDeployment = deployment;
        Microservices finalMicroservice = microservice;
        map.forEach((order, stage) -> {
            if (!stage.isEmpty()) {
                switch (order) {
                    case 1:
                        dev.set(createNonProdRow(stage.get(0), finalMicroservice, order));
                        break;
                    case 2:
                        test.set(createNonProdRow(stage.get(0), finalMicroservice, order));
                        break;
                    case 3:
                    case 4:
                        createAccProdRow(stage, order, finalDeployment, dto);
                        break;
                    default:
                        break;
                }
            }
        });
        if (dev.get().getState().equalsIgnoreCase("succeeded")) {
            microservice.setStatus("DEV");
        }
        if (test.get().getState().equalsIgnoreCase("succeeded")) {
            microservice.setStatus("TEST");
        }
        deployment.setDeploymentDev(dev.get());
        deployment.setDeploymentTest(test.get());

        deployment = deploymentRepo.save(deployment);

        microservice.setDeployment(deployment);
        microservice = microservicesRepo.save(microservice);

        return Mono.just(microservice);

    }

    public NonProd createNonProdRow(Record stage, Microservices microservice, Integer order) {
        Optional<NonProd> optionalNonProd = nonProdRepo.findByStageAndBuildNumber(stage.getBuildNumber(), order);
        if (optionalNonProd.isEmpty()) {
            NonProd nonProd = NonProd
                    .builder()
                    .stage(order)
                    .microserviceID(microservice.getId())
                    .buildNumber(stage.getBuildNumber())
                    .deploymentDate(stage.getFinishTime())
                    .state(stage.getResult())
                    .buildId(stage.getBuildID())
                    .build();
            nonProd = nonProdRepo.save(nonProd);
            if (log.isInfoEnabled()) {
                log.info("CREATED : NON_PROD_{} ROW -> [{}]", order == 1 ? "DEV" : "TEST", nonProd.getId());
            }
            return nonProd;
        } else {
            if (log.isInfoEnabled()) {
                log.info("DISCARDED : NON PROD ROW[{}] DATA ALREADY EXISTS", optionalNonProd.get().getId());
            }
            return optionalNonProd.get();
        }
    }

    public void createAccProdRow(List<Record> records, Integer order, Deployment finalDeployment, MicroservicesDTO dto) {

        records.forEach(stage -> {
            DocumentsDTO documentsDTO;
            Document document = new Document();
            if (dto.getDocuments() != null && !dto.getDocuments().isEmpty()
                    && dto.getDocuments().get(stage.getBuildNumber()) != null && Objects.equals(dto.getDocuments().get(stage.getBuildNumber()).getOrder(), order)) {
                documentsDTO = dto.getDocuments().get(stage.getBuildNumber());
                document = Document.builder()
//                        .iq(documentsDTO.getIq())
//                        .piq(documentsDTO.getPiq())
                        .signOffKt(documentsDTO.getSignOffKt())
                        .signOffSit(documentsDTO.getSignOffSit())
                        .signOffUat(documentsDTO.getSignOffUat())
                        .signOffPt(documentsDTO.getSignOffPt())
                        .signOffSt(documentsDTO.getSignOffSt())
//                        .azureVariables(documentsDTO.getAzureVariables())
                        .build();
            }

            Optional<AccProd> optionalAccProd = accProdRepo.findByStageAndBuildNumber(stage.getBuildNumber(), order);
            if (optionalAccProd.isEmpty()) {
                AccProd accProd = AccProd
                        .builder()
                        .stage(order)
                        .buildNumber(stage.getBuildNumber())
                        .deploymentDate(stage.getFinishTime())
                        .state(stage.getResult())
                        .buildId(stage.getBuildID())
                        .deployment(finalDeployment)
                        .documents(document)
                        .build();
                accProd = accProdRepo.save(accProd);
                if (log.isInfoEnabled()) {
                    log.info("CREATED : ACC_PROD_{} ROW -> [{}]", order == 3 ? "ACC" : "PROD", accProd.getId());
                }
            } else {

                if (optionalAccProd.get().getDocuments() != null) {
                    AccProd accProd = optionalAccProd.get();
                    accProd.setDocuments(document);
                    accProd = accProdRepo.save(accProd);
                    if (log.isInfoEnabled()) {
                        log.info("UPDATED : ACC PROD ROW[{}] WITH DOCUMENTS [{}]", accProd.getId(), accProd.getDocuments().getId());
                    }
                } else {
                    if (log.isInfoEnabled()) {
                        log.info("DISCARDED : ACC PROD ROW[{}] DATA ALREADY EXISTS", optionalAccProd.get().getId());
                    }
                }
            }
        });
    }

}
