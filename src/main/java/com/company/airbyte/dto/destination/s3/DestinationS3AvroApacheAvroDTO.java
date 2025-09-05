package com.company.airbyte.dto.destination.s3;

public class DestinationS3AvroApacheAvroDTO extends DestinationS3OutputFormat {

    private String codec;

    public DestinationS3CompressionCodec getCodec() {
        return codec == null ? null : DestinationS3CompressionCodec.fromId(codec);
    }

    public void setCodec(DestinationS3CompressionCodec codec) {
        this.codec = codec == null ? null : codec.getId();
    }

}
