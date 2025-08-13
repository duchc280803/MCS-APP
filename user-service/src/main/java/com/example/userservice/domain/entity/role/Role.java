package com.example.userservice.domain.entity.role;

public enum Role {

    ROLE_ADMIN("ADMIN"),
    ROLE_CUSTOMER("CUSTOMER"),
    ROLE_3("31"),
    ROLE_4("41"),
    ROLE_51("51"),
    ROLE_52("52"),
    ROLE_6("60");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
