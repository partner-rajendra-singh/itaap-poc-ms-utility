package com.philips.itaap.utility.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccProdDTO {
    private Integer stage;
    private String fileTag;
    private String fileData;
    private String fileName;
    private String buildNumber;
    private Integer buildId;
    private Integer pipelineID;
}
