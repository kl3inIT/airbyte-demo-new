package com.company.airbyte.dto.source.postgres;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum SourcePostgresInvalidCDCPositionBehaviorAdvanced implements EnumClass<String> {
    FAIL_SYNC("Fail sync"),
    RE_SYNC_DATA("Re-sync data");

    private final String id;

    SourcePostgresInvalidCDCPositionBehaviorAdvanced(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static SourcePostgresInvalidCDCPositionBehaviorAdvanced fromId(String id) {
        for (SourcePostgresInvalidCDCPositionBehaviorAdvanced at : SourcePostgresInvalidCDCPositionBehaviorAdvanced.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}