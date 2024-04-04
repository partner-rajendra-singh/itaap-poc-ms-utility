package com.philips.itaap.utility.controller;

import com.philips.itaap.utility.config.AzureProperties;
import com.philips.itaap.utility.entity.AzureVariablesEntity;
import com.philips.itaap.utility.model.AzureVariablesRequest;
import com.philips.itaap.utility.serivce.AzureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@Slf4j
@SuppressWarnings({"CPD-START"})
public class AzureAdminController {
    @Autowired
    AzureService azureService;

    @Autowired
    AzureProperties properties;

    @GetMapping(value = "/poc/fetchRequests/{approver}")
    public Mono<List<AzureVariablesEntity>> fetchRequests(@PathVariable String approver) {

        if (log.isInfoEnabled()) {
            log.info("fetchRequests() : approver : {}", approver);
        }

        return Mono.just(azureService.getRequests(approver));
    }

    @PostMapping(value = "/poc/approveRequest")
    public Mono<Boolean> approveRequest(@RequestBody AzureVariablesRequest request) {

        if (log.isInfoEnabled()) {
            log.info("approveRequest() : request : {}", request);
        }

        boolean response = azureService.approveRequest(request) != 0;
        return Mono.just(response);
    }

    @PostMapping(value = "/poc/rejectRequest")
    public Mono<Boolean> rejectRequest(@RequestBody AzureVariablesRequest request) {

        if (log.isInfoEnabled()) {
            log.info("rejectRequest() : request : {}", request);
        }

        boolean response = azureService.rejectRequest(request) != 0;
        return Mono.just(response);
    }

}
