package com.company.airbyte.dto.source.mssql;

import io.jmix.core.metamodel.datatype.EnumClass;
import org.springframework.lang.Nullable;

public enum SourceMssqlSSLMethodType implements EnumClass<String> {

    UNENCRYPTED("unencrypted"),
    TRUST("trust"),
    VERIFY("verify");

    private final String id;

    SourceMssqlSSLMethodType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static SourceMssqlSSLMethodType fromId(String id) {
        for (SourceMssqlSSLMethodType at : SourceMssqlSSLMethodType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
