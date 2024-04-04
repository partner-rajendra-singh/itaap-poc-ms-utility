package com.philips.itaap.utility.model.enums;

public enum Status {
    N("New"),
    A("Accepted"),
    R("Rejected"),
    C("Completed"),
    O("Overdue"),
    P("InProgress"),
    B("Blocked"),
    D("Deleted");

    private final String value;

    Status(String v) {
        value = v;
    }

    public String value() {
        return value;
    }
}
