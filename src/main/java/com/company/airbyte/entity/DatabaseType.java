package com.company.airbyte.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum DatabaseType implements EnumClass<String> {

    POSTGRES("postgres"),
    MYSQL("mysql"),
    MSSQL("mssql");

    private final String id;

    DatabaseType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static DatabaseType fromId(String id) {
        if (id == null) {
            return null;
        }
        String normalized = id.trim();
        for (DatabaseType at : DatabaseType.values()) {
            if (at.getId().equalsIgnoreCase(normalized) || at.name().equalsIgnoreCase(normalized)) {
                return at;
            }
        }
        return null;
    }
}
