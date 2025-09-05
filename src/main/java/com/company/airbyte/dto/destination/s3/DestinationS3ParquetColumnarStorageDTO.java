package com.company.airbyte.dto.destination.s3;

import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity
public class DestinationS3ParquetColumnarStorageDTO extends DestinationS3OutputFormat {

    private Long blockSizeMb;

    private String compressionCodec;

    private Boolean dictionaryEncoding;

    private Long dictionaryPageSizeKb;

    private Long maxPaddingSizeMb;

    private Long pageSizeKb;

    public DestinationS3SchemasCompressionCodec getCompressionCodec() {
        return compressionCodec == null ? null : DestinationS3SchemasCompressionCodec.fromId(compressionCodec);
    }

    public void setCompressionCodec(DestinationS3SchemasCompressionCodec compressionCodec) {
        this.compressionCodec = compressionCodec == null ? null : compressionCodec.getId();
    }

    public Boolean getDictionaryEncoding() {
        return dictionaryEncoding;
    }

    public void setDictionaryEncoding(Boolean dictionaryEncoding) {
        this.dictionaryEncoding = dictionaryEncoding;
    }

    public Long getDictionaryPageSizeKb() {
        return dictionaryPageSizeKb;
    }

    public void setDictionaryPageSizeKb(Long dictionaryPageSizeKb) {
        this.dictionaryPageSizeKb = dictionaryPageSizeKb;
    }

    public Long getMaxPaddingSizeMb() {
        return maxPaddingSizeMb;
    }

    public void setMaxPaddingSizeMb(Long maxPaddingSizeMb) {
        this.maxPaddingSizeMb = maxPaddingSizeMb;
    }

    public Long getPageSizeKb() {
        return pageSizeKb;
    }

    public void setPageSizeKb(Long pageSizeKb) {
        this.pageSizeKb = pageSizeKb;
    }

    public Long getBlockSizeMb() {
        return blockSizeMb;
    }

    public void setBlockSizeMb(Long blockSizeMb) {
        this.blockSizeMb = blockSizeMb;
    }
}