package com.philips.itaap.utility.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentsDTO {
    private Integer order;
    private String iq;
    private String piq;
    private Date signOffKt;
    private Date signOffSit;
    private Date signOffUat;
    private Date signOffPt;
    private Date signOffSt;
    private String azureVariables;
}
