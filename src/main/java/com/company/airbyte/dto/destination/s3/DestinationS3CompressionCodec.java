package com.company.airbyte.dto.destination.s3;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum DestinationS3CompressionCodec implements EnumClass<String> {
    NO_COMPRESSION("NO_COMPRESSION"),
    DEFLATE("DEFLATE"),
    BZIP2("BZIP2"),
    XZ("XZ"),
    ZSTANDARD("ZSTANDARD"),
    SNAPPY("SNAPPY");

    private final String id;

    DestinationS3CompressionCodec(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static DestinationS3CompressionCodec fromId(String id) {
        for (DestinationS3CompressionCodec at : DestinationS3CompressionCodec.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
