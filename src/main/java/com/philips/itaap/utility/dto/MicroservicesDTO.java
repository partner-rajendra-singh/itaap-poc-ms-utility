package com.philips.itaap.utility.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MicroservicesDTO {
    private Integer id;
    private String name;
    private String status;
    private Integer techLeadID;
    private Integer developerID;
    private Integer backupID;
    private Integer scrumMasterID;
    private Integer pocID;
    private Integer pipelineID;
    private Date targetCompletionDate;
    private Date tgl;
    private Date bgl;
    private String itd;
    private String postmanCollection;
    private Map<String, DocumentsDTO> documents;
}
