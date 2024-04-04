package com.philips.itaap.utility.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "AZURE_VARIABLES_REQUEST")
@AllArgsConstructor
@NoArgsConstructor
public class AzureVariablesEntity {

    @Id
    @GeneratedValue(generator = "azure_variables_seq")
    @Column(name = "ID", nullable = false)
    @JsonProperty("id")
    private Integer id;

    @Column(name = "REQUESTER")
    @JsonProperty("requester")
    private String requester;

    @Column(name = "APPROVER")
    @JsonProperty("approver")
    private String approver;

    @Column(name = "REQUEST_DATE")
    @JsonProperty("requestDate")
    private Date requestDate;

    @Column(name = "AZURE_GROUP_NAME")
    @JsonProperty("azureGroupName")
    private String azureGroupName;

    @Column(name = "UPDATED_ALL_VARIABLES")
    @JsonProperty("updatedAllVariables")
    private String updatedAllVariables;

    @Column(name = "MODIFIED_VARIABLES")
    @JsonProperty("modifiedVariables")
    private String modifiedVariables;

    @Column(name = "STATUS")
    @JsonProperty("status")
    private String status;

}
