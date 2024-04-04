package com.philips.itaap.utility.model.deployment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RunDetails {
    private String state;
    private String result;
    private Long id;
    private String name;
    private String url;
}
