package com.company.airbyte.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum ProviderUnit implements EnumClass<String> {

    MILITARY_PERSONNEL("Cục Quân số"),
    ENGINEERING("Cục Kỹ thuật"),
    POLITICAL_DEPARTMENT("Tổng cục Chính trị"),
    PERSONNEL_DEPARTMENT("Cục Tổ chức cán bộ"),
    TRAINING_DEPARTMENT("Cục Huấn luyện"),
    MILITARY_BORDERS("Cục Quân giới"),
    RESERVE_DEPARTMENT("Cục Dự bị động viên"),
    MILITARY_ACADEMY("Học viện Quân sự"),
    MEDICAL_DEPARTMENT("Cục Y tế"),
    GENERAL_STAFF("Bộ Tổng tham mưu"),
    LOGISTICS_DEPARTMENT("Cục Hậu cần");

    private final String id;

    ProviderUnit(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ProviderUnit fromId(String id) {
        for (ProviderUnit at : ProviderUnit.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}