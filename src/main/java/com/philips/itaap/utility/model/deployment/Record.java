package com.philips.itaap.utility.model.deployment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Record {
    private String id;
    private String type;
    private String name;
    private String state;
    private String result;
    private Date finishTime;
    private int order;
    private String icon;
    private String color;
    private String identifier;
    private String buildNumber;
    private Long buildID;
}
