package com.company.airbyte.dto.source.file;


import com.airbyte.api.models.shared.FileFormat;
import com.airbyte.api.models.shared.StorageProvider;

import com.company.airbyte.dto.source.SourceDTO;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.Objects;

@JmixEntity
public class SourceFileDTO extends SourceDTO {

    private String datasetName;

    private String format;

    private String provider;

    private String readerOptions;

    private String url;



    public StorageProviderType getProvider() {
        return provider == null ? null : StorageProviderType.fromId(provider);
    }

    public void setProvider(StorageProviderType provider) {
        this.provider = provider == null ? null : provider.getId();
    }

    public FileFormatType getFormat() {
        return format == null ? null : FileFormatType.fromId(format);
    }

    public void setFormat(FileFormatType format) {
        this.format = format == null ? null : format.getId();
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getReaderOptions() {
        return readerOptions;
    }

    public void setReaderOptions(String readerOptions) {
        this.readerOptions = readerOptions;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SourceFileDTO that = (SourceFileDTO) o;
        return Objects.equals(datasetName, that.datasetName)
                && Objects.equals(format, that.format)
                && Objects.equals(provider, that.provider)
                && Objects.equals(readerOptions, that.readerOptions)
                && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                datasetName, format, provider, readerOptions, url
        );
    }
}