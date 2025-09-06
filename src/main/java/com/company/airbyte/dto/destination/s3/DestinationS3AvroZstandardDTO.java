package com.company.airbyte.dto.destination.s3;

import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DestinationS3AvroZstandardDTO that = (DestinationS3AvroZstandardDTO) o;
        return Objects.equals(compressionLevel, that.compressionLevel)
                && Objects.equals(includeChecksum, that.includeChecksum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), compressionLevel, includeChecksum);
    }

}