package com.philips.itaap.utility.constant;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum RaquestStatus {

    @JsonProperty("Initiated")
    INITIATED("Initiated"),

    @JsonProperty("Approved")
    APPROVED("Approved"),

    @JsonProperty("Rejected")
    REJECTED("Rejected");

    private final String value;

    RaquestStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }
}
