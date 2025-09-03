package com.company.airbyte.dto.source.mysql.dto;

import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity
public class SourceMySQLVerifyDTO {
    private String caCertificate;

    private String clientCertificate;

    private String clientKey;

    private String clientKeyPassword;

    public String getCaCertificate() {
        return caCertificate;
    }

    public void setCaCertificate(String caCertificate) {
        this.caCertificate = caCertificate;
    }

    public String getClientCertificate() {
        return clientCertificate;
    }

    public void setClientCertificate(String clientCertificate) {
        this.clientCertificate = clientCertificate;
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    public String getClientKeyPassword() {
        return clientKeyPassword;
    }

    public void setClientKeyPassword(String clientKeyPassword) {
        this.clientKeyPassword = clientKeyPassword;
    }
}
