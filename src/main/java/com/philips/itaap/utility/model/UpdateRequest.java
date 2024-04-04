package com.philips.itaap.utility.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateRequest {
    private String description;
    private String name;
    private String type;
    private String id;

    @JsonProperty("variables")
    private Map<String, VariableValues> variables;
}
