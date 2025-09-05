package com.company.airbyte.dto.source.file;

import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.Objects;

@JmixEntity
public class SSH_SCP_SFTP_ProtocolDTO extends SourceFileStorageProviderDTO {

    private String host;

    private String password;

    private String port;

    private String user;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SSH_SCP_SFTP_ProtocolDTO that = (SSH_SCP_SFTP_ProtocolDTO) o;
        return Objects.equals(host, that.host)
                && Objects.equals(password, that.password)
                && Objects.equals(port, that.port)
                && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), host, password, port, user);
    }
}