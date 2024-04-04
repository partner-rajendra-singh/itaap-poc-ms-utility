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
public class TaskListDTO {
    private String taskDetails;
    private Date completedOn;
    private Integer completedByUserId;
    private Date addedOn;
    private boolean isCompleted;
}
