package com.company.airbyte.dto.source.file;

import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.Objects;

@JmixEntity
public class S3AmazonWebServicesDTO extends SourceFileStorageProviderDTO {

    private String awsAccessKeyId;

    private String awsSecretAccessKey;

    public String getAwsAccessKeyId() {
        return awsAccessKeyId;
    }

    public void setAwsAccessKeyId(String awsAccessKeyId) {
        this.awsAccessKeyId = awsAccessKeyId;
    }

    public String getAwsSecretAccessKey() {
        return awsSecretAccessKey;
    }

    public void setAwsSecretAccessKey(String awsSecretAccessKey) {
        this.awsSecretAccessKey = awsSecretAccessKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        S3AmazonWebServicesDTO that = (S3AmazonWebServicesDTO) o;
        return Objects.equals(awsAccessKeyId, that.awsAccessKeyId)
                && Objects.equals(awsSecretAccessKey, that.awsSecretAccessKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), awsAccessKeyId, awsSecretAccessKey);
    }
}