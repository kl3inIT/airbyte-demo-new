package com.company.airbyte.dto.source.mssql;
import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum SourceMssqlUpdateMethodType implements EnumClass<String> {
    CDC("CDC"),
    STANDARD("Standard");
    ;

    private final String id;

    SourceMssqlUpdateMethodType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static SourceMssqlUpdateMethodType fromId(String id) {
        for (SourceMssqlUpdateMethodType at : SourceMssqlUpdateMethodType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}