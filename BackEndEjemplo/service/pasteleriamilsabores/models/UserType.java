package com.service.pasteleriamilsabores.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserType {
    SUPER_ADMIN("SuperAdmin"),
    ADMINISTRADOR("Administrador"),
    VENDEDOR("Vendedor"),
    CLIENTE("Cliente");

    private final String value;

    UserType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static UserType fromString(String v) {
        if (v == null) return null;
        for (UserType t : UserType.values()) {
            if (t.value.equalsIgnoreCase(v) || t.name().equalsIgnoreCase(v)) return t;
        }
        throw new IllegalArgumentException("Unknown user type: " + v);
    }
}
