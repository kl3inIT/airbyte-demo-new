package com.company.airbyte.dto.source.file;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum StorageProviderType implements EnumClass<String> {
    HTTPS("HTTPS"),
    GCS("GCS"),
    S3("S3"),
    AZ_BLOB("AZ_BLOB"),
    SSH("SSH"),
    SCP("SCP"),
    SFTP("SFTP"),
    LOCAL("local");

    private final String id;

    StorageProviderType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static StorageProviderType fromId(String id) {
        for (StorageProviderType at : StorageProviderType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}