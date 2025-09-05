package com.company.airbyte.dto.destination.s3;

import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity
public class DestinationS3AvroXzDTO extends DestinationS3AvroCompressionCodecDTO {

    private Long compressionLevel;

    public Long getCompressionLevel() {
        return compressionLevel;
    }

    public void setCompressionLevel(Long compressionLevel) {
        this.compressionLevel = compressionLevel;
    }
}