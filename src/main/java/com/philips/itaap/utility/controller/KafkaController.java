package com.philips.itaap.utility.controller;

import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@XSlf4j
@SuppressWarnings({"CPD-START"})
public class KafkaController {
    @Autowired
    private ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate;

    @PostMapping(value = "/poc/kafka/post/message", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> updateDocuments(@RequestBody String message, @RequestParam(name = "topic") String topic) {
        return reactiveKafkaProducerTemplate
                .send(topic, message)
                .doOnSuccess(senderResult -> {
                    if (log.isInfoEnabled()) {
                        log.info("publish() : Topic -> {}, Partition -> {}, Offset -> {}", senderResult.recordMetadata().topic(), senderResult.recordMetadata().partition(), senderResult.recordMetadata().offset());
                    }
                })
                .then(Mono.just("Successfully Pushed to " + topic));
    }
}
