package com.company.airbyte.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum SourceType implements EnumClass<String> {

    DATABASE("DATABASE"),
    API("API"),
    FILE("FILE");

    private final String id;

    SourceType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static SourceType fromId(String id) {
        for (SourceType at : SourceType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}