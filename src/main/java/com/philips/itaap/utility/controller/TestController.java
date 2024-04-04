package com.philips.itaap.utility.controller;

import com.philips.itaap.ms.dev.base.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class TestController {
    @PostMapping(value = "/poc/test")
    public Mono<String> updateDocuments(@RequestBody String str) {
        if (str.contains("B")) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, 1001, "IllegalExceptionOccurred");
        }
        return Mono.just(str);
    }
}
