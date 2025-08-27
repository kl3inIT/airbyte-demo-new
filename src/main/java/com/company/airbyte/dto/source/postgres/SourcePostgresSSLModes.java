package com.company.airbyte.dto.source.postgres;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;

public enum SourcePostgresSSLModes implements EnumClass<String> {
    DISABLE("disable"),
    ALLOW("allow"),
    PREFER("prefer"),
    REQUIRE("require"),
    VERIFY_CA("verify-ca"),
    VERIFY_FULL("verify-full");

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
