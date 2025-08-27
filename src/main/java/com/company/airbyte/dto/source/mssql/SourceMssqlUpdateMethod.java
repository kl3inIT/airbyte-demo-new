package com.company.airbyte.dto.source.mssql;
import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum SourceMssqlUpdateMethod implements EnumClass<String> {
    CDC("CDC"),
    STANDARD("Standard");
    ;

    private final String id;

    SourceMssqlUpdateMethod(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static SourceMssqlUpdateMethod fromId(String id) {
        for (SourceMssqlUpdateMethod at : SourceMssqlUpdateMethod.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}