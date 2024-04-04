package com.philips.itaap.utility.controller;

import com.philips.itaap.utility.config.AzureProperties;
import com.philips.itaap.utility.constant.AzureConstants;
import com.philips.itaap.utility.dto.MicroservicesDTO;
import com.philips.itaap.utility.entity.Microservices;
import com.philips.itaap.utility.serivce.DatabaseService;
import com.philips.itaap.utility.serivce.DeploymentService;
import com.philips.itaap.utility.utils.Transformer;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@XSlf4j
@SuppressWarnings({"CPD-START"})
public class DeploymentController {

    @Autowired
    private DeploymentService deploymentService;
    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private AzureProperties properties;

    @GetMapping(value = "/poc/build/folders")
    public Mono<String> getAllBuildFolders(@RequestParam(name = "organization") String organization) {
        String uri = properties.getAllBuildFolderUrl().replace(AzureConstants.ORG_PLACEHOLDER, organization);
        if (log.isInfoEnabled()) {
            log.info("FETCHED : ALL BUILD FOLDERS : URL -> {}", uri);
        }
        return deploymentService.fetchDetails(uri)
                .map(string -> deploymentService.createTreeStructure(string));
    }

    @GetMapping(value = "/poc/pipeline/runs")
    public Mono<String> getRunsFromBuildID(@RequestParam(name = "organization") String organization,
                                           @RequestParam(name = "pipelineID") String pipelineID) {
        String uri = properties.getRunsFromBuildID().replace(AzureConstants.ORG_PLACEHOLDER, organization)
                .replace(AzureConstants.BUILD_ID_PLACEHOLDER, pipelineID);
        if (log.isInfoEnabled()) {
            log.info("FETCHED : ALL RUNS : PIPELINE_ID -> {} : URL -> {}", pipelineID, uri);
        }
        return deploymentService.fetchDetails(uri)
                .flatMap(string -> deploymentService.createTableStructure(string))
                .map(Transformer::getJSONString);
    }

    @GetMapping(value = "/poc/build/timeline")
    public Mono<String> getTimelineForBuild(@RequestParam(name = "organization") String organization,
                                            @RequestParam(name = "buildID") String buildID,
                                            @RequestParam(name = "buildNumber") String buildNumber) {
        String uri = properties.getGetBuildTimeline().replace(AzureConstants.ORG_PLACEHOLDER, organization)
                .replace(AzureConstants.BUILD_ID_PLACEHOLDER, buildID);
        if (log.isInfoEnabled()) {
            log.info("FETCHED : ALL TIMELINE : BUILD_ID -> {} : URL -> {}", buildID, uri);
        }
        return deploymentService.fetchDetails(uri)
                .map(string -> deploymentService.createTimeLineStructure(string, buildNumber));
    }

    @PostMapping(value = "/poc/update/database/microservice")
    public Mono<Microservices> populateMsDB(@RequestParam(name = "organization") String organization,
                                            @RequestParam(name = "pipelineID") String pipelineID,
                                            @RequestBody MicroservicesDTO microservice) {
        String uri = properties.getRunsFromBuildID().replace(AzureConstants.ORG_PLACEHOLDER, organization)
                .replace(AzureConstants.BUILD_ID_PLACEHOLDER, pipelineID);
        if (log.isInfoEnabled()) {
            log.info("UPDATE : MICROSERVICE : -> {} : URL -> {}", pipelineID, uri);
        }

        return deploymentService.fetchDetails(uri)
                .flatMap(string -> deploymentService.createTableStructure(string))
                .flatMap(runDetails -> deploymentService.populateMsData(runDetails))
                .flatMap(map -> databaseService.insertInDB(map, Integer.parseInt(pipelineID), microservice));
    }

    @PostMapping(value = "/poc/replace/scripts")
    public Mono<List<Map<String, String>>> replaceData(@RequestBody List<Map<String, String>> data,
                                                       @RequestParam(name = "MICROSERVICE_CD_NAME") String cdName,
                                                       @RequestParam(name = "MICROSERVICE_NAME") String msName,
                                                       @RequestParam(name = "BUILD_NUMBER") String buildNumber,
                                                       @RequestParam(name = "PLATFORM_NAME") String platform
    ) {
        return deploymentService.replaceScriptsData(data, cdName, msName, buildNumber, platform);
    }

}
