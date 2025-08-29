package com.company.airbyte.dto.source.postgres;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;

public enum LSNCommitBehaviour implements EnumClass<String> {
    WHILE_READING_DATA("WHILE_READING_DATA"),
    AFTER_LOADING_DATA_IN_THE_DESTINATION("AFTER_LOADING_DATA_IN_THE_DESTINATION");

    private final String id;

    LSNCommitBehaviour(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static LSNCommitBehaviour fromId(String id) {
        for (LSNCommitBehaviour at : LSNCommitBehaviour.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
