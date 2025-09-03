package com.company.airbyte.dto.source.postgres;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;

public enum SourcePostgresSSLModesType implements EnumClass<String> {
    DISABLE("DISABLE"),
    ALLOW("ALLOW"),
    PREFER("PREFER"),
    REQUIRE("REQUIRE"),
    VERIFY_CA("VERIFY_CA"),
    VERIFY_FULL("VERIFY_FULL");

    private final String id;

    SourcePostgresSSLModesType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static SourcePostgresSSLModesType fromId(String id) {
        for (SourcePostgresSSLModesType at : SourcePostgresSSLModesType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
