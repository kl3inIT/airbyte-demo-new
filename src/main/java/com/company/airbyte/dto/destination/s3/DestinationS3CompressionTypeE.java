package com.company.airbyte.dto.destination.s3;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum DestinationS3CompressionTypeE implements EnumClass<String> {

    NO_COMPRESSION("NO_COMPRESSION"),
    GZIP("GZIP");

    private final String id;

    DestinationS3CompressionTypeE(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static DestinationS3CompressionTypeE fromId(String id) {
        for (DestinationS3CompressionTypeE at : DestinationS3CompressionTypeE.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}