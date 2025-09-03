package com.company.airbyte.dto.source.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Entity;

import java.util.Optional;
import java.util.UUID;

@JmixEntity
public class GCSGoogleCloudStorageDTO {

    private String serviceAccountJson;

}