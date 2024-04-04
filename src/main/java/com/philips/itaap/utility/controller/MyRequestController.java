package com.philips.itaap.utility.controller;

import com.philips.itaap.utility.entity.AzureVariablesEntity;
import com.philips.itaap.utility.serivce.AzureService;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@XSlf4j
public class MyRequestController {

    @Autowired
    AzureService azureService;

    @GetMapping(value = "/poc/my-requests/{requester}")
    public Mono<List<AzureVariablesEntity>> getMyRequests(@PathVariable String requester) {

        if (log.isInfoEnabled()) {
            log.info("getMyRequests() : requester : {}", requester);
        }

        return Mono.just(azureService.getMyRequests(requester));
    }

}
