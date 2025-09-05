package com.company.airbyte.dto.source.file;

import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.Objects;

@JmixEntity
public class AzBlobAzureBlobStorageDTO extends SourceFileStorageProviderDTO {

    private String sasToken;

    private String sharedKey;

    private String storageAccount;

    public String getSasToken() {
        return sasToken;
    }

    public void setSasToken(String sasToken) {
        this.sasToken = sasToken;
    }

    public String getSharedKey() {
        return sharedKey;
    }

    public void setSharedKey(String sharedKey) {
        this.sharedKey = sharedKey;
    }

    public String getStorageAccount() {
        return storageAccount;
    }

    public void setStorageAccount(String storageAccount) {
        this.storageAccount = storageAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AzBlobAzureBlobStorageDTO that = (AzBlobAzureBlobStorageDTO) o;
        return Objects.equals(sasToken, that.sasToken)
                && Objects.equals(sharedKey, that.sharedKey)
                && Objects.equals(storageAccount, that.storageAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sasToken, sharedKey, storageAccount);
    }
}