package com.philips.itaap.utility.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@XSlf4j
@SuppressWarnings("PMD")
public class Transformer {

    public static <T> T readValue(String path, Class<T> type) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(path, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static <T> T readValue(String path, TypeReference<T> type) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(path, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static <T> String getJSONString(T object) {
        if (object != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writeValueAsString(object);
            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error("Error occurred [{}] while converting to string [{}]", e.getMessage(), object);
                }
            }
        }
        return "";
    }

    public static <T> T readValueFromResource(String path, TypeReference<T> type) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new ClassPathResource(path).getInputStream(), type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
