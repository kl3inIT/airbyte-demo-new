package com.company.airbyte.dto.source.file;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Entity;

import java.util.Optional;
import java.util.UUID;

@JmixEntity
public class HTTPSPublicWebDTO {

    private Boolean userAgent;

    public Boolean getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(Boolean userAgent) {
        this.userAgent = userAgent;
    }
}