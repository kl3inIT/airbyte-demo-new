package com.company.airbyte.dto.destination.s3;

import com.company.airbyte.dto.source.file.S3AmazonWebServicesDTO;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DestinationS3AvroApacheAvroDTO that = (DestinationS3AvroApacheAvroDTO) o;
        return Objects.equals(codec, that.codec)
                && Objects.equals(formatCodec, that.formatCodec);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), codec, formatCodec);
    }
}
