package com.company.airbyte.dto.source.postgres;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum SourcePostgresInvalidCDCPositionBehaviorAdvancedType implements EnumClass<String> {
    FAIL_SYNC("FAIL_SYNC"),
    RE_SYNC_DATA("RE_SYNC_DATA");

    private final String id;

    SourcePostgresInvalidCDCPositionBehaviorAdvancedType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static SourcePostgresInvalidCDCPositionBehaviorAdvancedType fromId(String id) {
        for (SourcePostgresInvalidCDCPositionBehaviorAdvancedType at : SourcePostgresInvalidCDCPositionBehaviorAdvancedType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}