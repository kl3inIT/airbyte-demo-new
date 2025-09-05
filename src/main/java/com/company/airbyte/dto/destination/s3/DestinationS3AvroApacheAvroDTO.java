package com.company.airbyte.dto.destination.s3;

import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity
public class DestinationS3AvroApacheAvroDTO extends DestinationS3OutputFormat {

    private String codec;

    private DestinationS3AvroCompressionCodecDTO formatCodec;

    public DestinationS3AvroCompressionCodecDTO getFormatCodec() {
        return formatCodec;
    }

    public void setFormatCodec(DestinationS3AvroCompressionCodecDTO formatCodec) {
        this.formatCodec = formatCodec;
    }

    public DestinationS3CompressionCodec getCodec() {
        return codec == null ? null : DestinationS3CompressionCodec.fromId(codec);
    }

    public void setCodec(DestinationS3CompressionCodec codec) {
        this.codec = codec == null ? null : codec.getId();
    }

}
