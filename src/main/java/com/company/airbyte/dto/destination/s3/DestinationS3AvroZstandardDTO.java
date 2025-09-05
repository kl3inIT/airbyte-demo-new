package com.company.airbyte.dto.destination.s3;

import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity
public class DestinationS3AvroZstandardDTO extends DestinationS3AvroCompressionCodecDTO {

    private Long compressionLevel;

    private Boolean includeChecksum;

    public Long getCompressionLevel() {
        return compressionLevel;
    }

    public void setCompressionLevel(Long compressionLevel) {
        this.compressionLevel = compressionLevel;
    }

    public Boolean getIncludeChecksum() {
        return includeChecksum;
    }

    public void setIncludeChecksum(Boolean includeChecksum) {
        this.includeChecksum = includeChecksum;
    }
}