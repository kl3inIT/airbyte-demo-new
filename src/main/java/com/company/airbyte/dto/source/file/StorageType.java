package com.company.airbyte.dto.source.file;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum StorageType implements EnumClass<String> {
    HTTPS("HTTPS"),
    GCS("GCS"),
    S3("S3"),
    AZ_BLOB("AzBlob"),
    SSH("SSH"),
    SCP("SCP"),
    SFTP("SFTP"),
    LOCAL("local");

    private final String id;

    StorageType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static StorageType fromId(String id) {
        for (StorageType at : StorageType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}