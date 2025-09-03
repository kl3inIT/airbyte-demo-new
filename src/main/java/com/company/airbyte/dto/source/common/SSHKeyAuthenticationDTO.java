package com.company.airbyte.dto.source.common;

import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity
public class SSHKeyAuthenticationDTO extends  SourceSSHTunnelMethodDTO {
    private String tunnelHost;

    private Long tunnelPort;

    private String tunnelUser;

    private String sshKey;

    public String getTunnelHost() {
        return tunnelHost;
    }

    public void setTunnelHost(String tunnelHost) {
        this.tunnelHost = tunnelHost;
    }

    public Long getTunnelPort() {
        return tunnelPort;
    }

    public void setTunnelPort(Long tunnelPort) {
        this.tunnelPort = tunnelPort;
    }

    public String getTunnelUser() {
        return tunnelUser;
    }

    public void setTunnelUser(String tunnelUser) {
        this.tunnelUser = tunnelUser;
    }

    public String getSshKey() {
        return sshKey;
    }

    public void setSshKey(String sshKey) {
        this.sshKey = sshKey;
    }
}
