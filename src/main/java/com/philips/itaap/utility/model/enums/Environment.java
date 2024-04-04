package com.philips.itaap.utility.model.enums;

public enum Environment {
    D("Development"),
    T("Test"),
    A("Acceptance"),
    P("Production");

    private final String value;

    Environment(String v) {
        value = v;
    }

    public String value() {
        return value;
    }
}
