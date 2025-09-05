package com.company.airbyte.dto.source.file;

import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.Objects;

@JmixEntity
public class GCSGoogleCloudStorageDTO extends SourceFileStorageProviderDTO {

    private String serviceAccountJson;

    public String getServiceAccountJson() {
        return serviceAccountJson;
    }

    public void setServiceAccountJson(String serviceAccountJson) {
        this.serviceAccountJson = serviceAccountJson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GCSGoogleCloudStorageDTO that = (GCSGoogleCloudStorageDTO) o;
        return Objects.equals(serviceAccountJson, that.serviceAccountJson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), serviceAccountJson);
    }
}