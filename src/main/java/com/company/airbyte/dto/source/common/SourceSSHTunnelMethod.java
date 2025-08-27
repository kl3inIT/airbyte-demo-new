package com.company.airbyte.dto.source.common;

import org.springframework.lang.Nullable;

public enum SourceSSHTunnelMethod {
    NO_TUNNEL("NO_TUNNEL"),
    SSH_KEY_AUTH("SSH_KEY_AUTH"),
    SSH_PASSWORD_AUTH("SSH_PASSWORD_AUTH");

    private final String id;

    SourceSSHTunnelMethod(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static SourceSSHTunnelMethod fromId(String id) {
        for (SourceSSHTunnelMethod at : SourceSSHTunnelMethod.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
