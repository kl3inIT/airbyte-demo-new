package com.company.airbyte.dto.destination.s3;

import io.jmix.core.metamodel.annotation.JmixEntity;


@JmixEntity
public class DestinationS3JSONLinesNewlineDelimitedJSONDTO extends DestinationS3OutputFormat{

    private String compression;

    private String flattening;

    public DestinationS3FlatteningType getFlattening() {
        return flattening == null ? null : DestinationS3FlatteningType.fromId(flattening);
    }

    public void setFlattening(DestinationS3FlatteningType flattening) {
        this.flattening = flattening == null ? null : flattening.getId();
    }

    public DestinationS3CompressionTypeE getCompression() {
        return compression == null ? null : DestinationS3CompressionTypeE.fromId(compression);
    }

    public void setCompression(DestinationS3CompressionTypeE compression) {
        this.compression = compression == null ? null : compression.getId();
    }

}
