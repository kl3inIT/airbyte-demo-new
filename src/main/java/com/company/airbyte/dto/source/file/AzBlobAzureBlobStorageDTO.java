package com.company.airbyte.dto.source.file;

import com.airbyte.api.models.shared.SourceFileSchemasProviderStorage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Entity;

import java.util.Optional;
import java.util.UUID;

@JmixEntity
public class AzBlobAzureBlobStorageDTO {

    private String sasToken;

    private String sharedKey;

    private String storageAccount;


}