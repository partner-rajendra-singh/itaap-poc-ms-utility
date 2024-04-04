package com.philips.itaap.utility.config;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.kafka.sender.SenderOptions;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Map;

@Component
public class AzureConfig {

    @Bean
    public ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate(KafkaProperties properties) {
        Map<String, Object> props = properties.buildProducerProperties();
        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(props));
    }

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(30));
        return WebClient
                .builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(5242880))
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
