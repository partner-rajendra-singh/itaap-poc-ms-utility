package com.philips.itaap.utility.controller;

import com.philips.itaap.utility.dto.MicroservicesDTO;
import com.philips.itaap.utility.entity.Microservices;
import com.philips.itaap.utility.serivce.MsService;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RestController
@XSlf4j
@SuppressWarnings({"CPD-START"})
public class MicroserviceController {

    @Autowired
    private MsService msService;

    @PostMapping(value = "/poc/create/microservice", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Microservices> createNewMicroservice(@RequestBody MicroservicesDTO microservices) {
        return Mono.just(msService.createOrUpdateMicroservice(microservices));
    }

    @GetMapping(value = "/poc/fetch/microservices/all")
    public Mono<List<Microservices>> getAllMicroservices() {
        return msService.getAllMicroservices();
    }
    @GetMapping(value = "/poc/fetch/microservices/name")
    public Mono<Optional<Microservices>> getMicroserviceById(@RequestParam(name = "microserviceName") String microserviceName) {
        return msService.getMicroserviceByName(microserviceName);
    }

}
