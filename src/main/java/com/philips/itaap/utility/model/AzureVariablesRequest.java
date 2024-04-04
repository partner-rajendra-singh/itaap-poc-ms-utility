package com.philips.itaap.utility.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.philips.itaap.utility.constant.RaquestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AzureVariablesRequest {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("requester")
    private String requester;

    @JsonProperty("approver")
    private String approver;

    @JsonProperty("requestDate")
    private Date requestDate;

    @JsonProperty("azureGroupName")
    private String azureGroupName;

    @JsonProperty("updatedAllVariables")
    private List<VariableValues> updatedAllVariables;

    @JsonProperty("modifiedVariables")
    private List<VariableValues> modifiedVariables;

    @JsonProperty("status")
    private RaquestStatus status;

}
