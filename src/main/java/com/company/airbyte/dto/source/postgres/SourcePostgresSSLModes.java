package com.company.airbyte.dto.source.postgres;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;

public enum SourcePostgresSSLModes implements EnumClass<String> {
    DISABLE("DISABLE"),
    ALLOW("ALLOW"),
    PREFER("PREFER"),
    REQUIRE("REQUIRE"),
    VERIFY_CA("VERIFY_CA"),
    VERIFY_FULL("VERIFY_FULL");

    private final String id;

    SourcePostgresSSLModes(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static SourcePostgresSSLModes fromId(String id) {
        for (SourcePostgresSSLModes at : SourcePostgresSSLModes.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
