package com.company.airbyte.dto.source.common;

import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity
public class SSH_SCP_SFTP_ProtocolDTO {

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
}