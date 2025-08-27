package com.company.airbyte.dto.source.postgres;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;

public enum SourcePostgresUpdateMethod implements EnumClass<String> {
    CDC("CDC"),
    XMIN("Xmin"),
    STANDARD("Standard");

    private final String id;

    SourcePostgresUpdateMethod(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static SourcePostgresUpdateMethod fromId(String id) {
        for (SourcePostgresUpdateMethod at : SourcePostgresUpdateMethod.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
