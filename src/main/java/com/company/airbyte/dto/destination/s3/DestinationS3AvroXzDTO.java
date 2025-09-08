package com.company.airbyte.dto.destination.s3;

import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.Objects;

@JmixEntity
public class DestinationS3AvroXzDTO extends DestinationS3AvroCompressionCodecDTO {

    private Long compressionLevel;

    public Long getCompressionLevel() {
        return compressionLevel;
    }

    public void setCompressionLevel(Long compressionLevel) {
        this.compressionLevel = compressionLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DestinationS3AvroXzDTO that = (DestinationS3AvroXzDTO) o;
        return Objects.equals(compressionLevel, that.compressionLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), compressionLevel);
    }

}