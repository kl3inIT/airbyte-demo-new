package com.company.airbyte.dto.source.file;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum FileFormatType implements EnumClass<String> {
    CSV("csv"),
    JSON("json"),
    JSONL("jsonl"),
    EXCEL("excel"),
    EXCEL_BINARY("excel_binary"),
    FWF("fwf"),
    FEATHER("feather"),
    PARQUET("parquet"),
    YAML("yaml");

    private final String id;

    FileFormatType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static FileFormatType fromId(String id) {
        for (FileFormatType at : FileFormatType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}