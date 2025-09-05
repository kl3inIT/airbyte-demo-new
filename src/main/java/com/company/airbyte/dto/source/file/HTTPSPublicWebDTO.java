package com.company.airbyte.dto.source.file;

import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.Objects;

@JmixEntity
public class HTTPSPublicWebDTO extends SourceFileStorageProviderDTO {

    private Boolean userAgent;

    public Boolean getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(Boolean userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        HTTPSPublicWebDTO that = (HTTPSPublicWebDTO) o;
        return Objects.equals(userAgent, that.userAgent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userAgent);
    }
}