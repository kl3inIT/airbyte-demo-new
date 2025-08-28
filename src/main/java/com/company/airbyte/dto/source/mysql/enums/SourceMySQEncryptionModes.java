package com.company.airbyte.dto.source.mysql.enums;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum SourceMySQEncryptionModes implements EnumClass<String> {

    PREFER("prefer"),
    REQUIRE("require"),
    VERIFY_CA("verify-ca"),
    VERIFY_FULL("verify-identity")
    ;

    private final String id;

    SourceMySQEncryptionModes(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static SourceMySQEncryptionModes fromId(String id) {
        for (SourceMySQEncryptionModes at : SourceMySQEncryptionModes.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}