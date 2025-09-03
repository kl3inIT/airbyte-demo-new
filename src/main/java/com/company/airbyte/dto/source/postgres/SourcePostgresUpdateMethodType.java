package com.company.airbyte.dto.source.postgres;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;

public enum SourcePostgresUpdateMethodType implements EnumClass<String> {
    CDC("CDC"),
    XMIN("XMIN"),
    STANDARD("STANDARD");

    private final String id;

    SourcePostgresUpdateMethodType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static SourcePostgresUpdateMethodType fromId(String id) {
        for (SourcePostgresUpdateMethodType at : SourcePostgresUpdateMethodType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
