package com.company.airbyte.dto.destination.s3;

import com.company.airbyte.dto.source.file.SSH_SCP_SFTP_ProtocolDTO;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.Objects;

@JmixEntity
public class DestinationS3CSVCommaSeparatedValuesDTO extends DestinationS3OutputFormat {

    private String compression;

    private String flattening;

    public DestinationS3CompressionTypeE getCompression() {
        return compression == null ? null : DestinationS3CompressionTypeE.fromId(compression);
    }

    public void setCompression(DestinationS3CompressionTypeE compression) {
        this.compression = compression == null ? null : compression.getId();
    }

    public DestinationS3FlatteningType getFlattening() {
        return flattening == null ? null : DestinationS3FlatteningType.fromId(flattening);
    }

    public void setFlattening(DestinationS3FlatteningType flattening) {
        this.flattening = flattening == null ? null : flattening.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DestinationS3CSVCommaSeparatedValuesDTO that = (DestinationS3CSVCommaSeparatedValuesDTO) o;
        return Objects.equals(compression, that.compression)
                && Objects.equals(flattening, that.flattening);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), compression, flattening);
    }
}
