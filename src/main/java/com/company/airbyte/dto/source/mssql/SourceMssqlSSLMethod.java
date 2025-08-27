package com.company.airbyte.dto.source.mssql;

import io.jmix.core.metamodel.datatype.EnumClass;
import org.springframework.lang.Nullable;

public enum SourceMssqlSSLMethod implements EnumClass<String> {

    UNENCRYPTED("unencrypted"),
    TRUST("trust"),
    VERIFY("verify");

    private final String id;

    SourceMssqlSSLMethod(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static SourceMssqlSSLMethod fromId(String id) {
        for (SourceMssqlSSLMethod at : SourceMssqlSSLMethod.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
