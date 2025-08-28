package com.company.airbyte.dto.source.mysql.enums;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum SourceMySQLUpdateMethod implements EnumClass<String> {
    STANDARD("Standard"),
    CDC("CDC");


    private final String id;

    SourceMySQLUpdateMethod(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static SourceMySQLUpdateMethod fromId(String id) {
        for (SourceMySQLUpdateMethod at : SourceMySQLUpdateMethod.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}