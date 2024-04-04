package com.philips.itaap.utility.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * This is a class used to define custom exception.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("EXCEPTION")
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class AzureException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    @JsonProperty("$id")
    private String id;

    @JsonProperty("innerException")
    private String innerException;

    @JsonProperty("message")
    private String message;

    @JsonProperty("typeName")
    private String typeName;

    @JsonProperty("typeKey")
    private String typeKey;

    @JsonProperty("errorCode")
    private int errorCode;

    @JsonProperty("eventId")
    private int eventId;
}