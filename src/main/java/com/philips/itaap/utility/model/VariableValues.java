package com.philips.itaap.utility.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VariableValues {
    private int index;
    private String key;
    private String value;
    private boolean isSecret;
    private boolean isEncrypted;
}
