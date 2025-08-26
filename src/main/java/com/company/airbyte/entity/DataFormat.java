package com.company.airbyte.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum DataFormat implements EnumClass<String> {

    QUERY("QUERY"),
    TABLE("TABLE"),
    STREAM("STREAM");

    private final String id;

    DataFormat(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static DataFormat fromId(String id) {
        for (DataFormat at : DataFormat.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}