package com.philips.itaap.utility.model.enums;

public enum Priority {
    C("Critical"),
    H("High"),
    M("Medium"),
    L("Low");

    private final String value;

    Priority(String v) {
        value = v;
    }

    public String value() {
        return value;
    }
}
