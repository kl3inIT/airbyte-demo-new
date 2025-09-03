package com.company.airbyte.dto.source.postgres;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;

public enum LSNCommitBehaviourType implements EnumClass<String> {
    WHILE_READING_DATA("WHILE_READING_DATA"),
    AFTER_LOADING_DATA_IN_THE_DESTINATION("AFTER_LOADING_DATA_IN_THE_DESTINATION");

    private final String id;

    LSNCommitBehaviourType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static LSNCommitBehaviourType fromId(String id) {
        for (LSNCommitBehaviourType at : LSNCommitBehaviourType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
