package com.company.airbyte.dto.destination.s3;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum DestinationS3FlatteningType implements EnumClass<String> {

    NO_FLATTENING("No flattening"),
    ROOT_LEVEL_FLATTENING("Root level flattening");

    private final String id;

    DestinationS3FlatteningType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static DestinationS3FlatteningType fromId(String id) {
        for (DestinationS3FlatteningType at : DestinationS3FlatteningType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
