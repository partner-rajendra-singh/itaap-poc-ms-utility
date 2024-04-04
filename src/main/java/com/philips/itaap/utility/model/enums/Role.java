package com.philips.itaap.utility.model.enums;

public enum Role {
    A("Admin"),
    T("Tech Lead"),
    D("Developer"),
    M("Scrum Master"),
    R("Read"),
    C("Client");

    private final String value;

    Role(String v) {
        value = v;
    }

    public String value() {
        return value;
    }
}
