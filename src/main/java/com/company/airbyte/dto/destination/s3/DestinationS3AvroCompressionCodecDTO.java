package com.company.airbyte.dto.destination.s3;

import com.company.airbyte.dto.source.file.SourceFileStorageProviderDTO;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.Objects;
import java.util.UUID;

@JmixEntity
public class DestinationS3AvroCompressionCodecDTO {

    @JmixGeneratedValue
    @JmixId
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DestinationS3AvroCompressionCodecDTO that = (DestinationS3AvroCompressionCodecDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}