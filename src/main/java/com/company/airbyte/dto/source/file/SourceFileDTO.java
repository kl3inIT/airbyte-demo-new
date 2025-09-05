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

    private S3AmazonWebServicesDTO s3;
    private GCSGoogleCloudStorageDTO gcs;
    private AzBlobAzureBlobStorageDTO azBlob;
    private HTTPSPublicWebDTO https;
    private SSH_SCP_SFTP_ProtocolDTO sshLike;

    public S3AmazonWebServicesDTO getS3() {
        return s3;
    }

    public void setS3(S3AmazonWebServicesDTO s3) {
        this.s3 = s3;
    }

    public GCSGoogleCloudStorageDTO getGcs() {
        return gcs;
    }

    public void setGcs(GCSGoogleCloudStorageDTO gcs) {
        this.gcs = gcs;
    }

    public AzBlobAzureBlobStorageDTO getAzBlob() {
        return azBlob;
    }

    public void setAzBlob(AzBlobAzureBlobStorageDTO azBlob) {
        this.azBlob = azBlob;
    }

    public HTTPSPublicWebDTO getHttps() {
        return https;
    }

    public void setHttps(HTTPSPublicWebDTO https) {
        this.https = https;
    }

    public SSH_SCP_SFTP_ProtocolDTO getSshLike() {
        return sshLike;
    }

    public void setSshLike(SSH_SCP_SFTP_ProtocolDTO sshLike) {
        this.sshLike = sshLike;
    }

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
                && Objects.equals(url, that.url)
                && Objects.equals(s3, that.s3)
                && Objects.equals(gcs, that.gcs)
                && Objects.equals(azBlob, that.azBlob)
                && Objects.equals(https, that.https)
                && Objects.equals(sshLike, that.sshLike);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                datasetName, format, provider, readerOptions, url, s3, gcs, azBlob, https, sshLike
        );
    }
}