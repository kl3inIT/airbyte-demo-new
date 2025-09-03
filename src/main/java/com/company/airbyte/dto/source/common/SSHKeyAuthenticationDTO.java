package com.company.airbyte.dto.source.common;

import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JmixEntity
public class SSHKeyAuthenticationDTO extends  SourceSSHTunnelMethodDTO {
    @NotBlank
    private String tunnelHost;

    @NotBlank
    private Integer tunnelPort = 22;

    @NotBlank
    private String tunnelUser;

    @NotBlank
    private String tunnelUserPassword;

    public String getTunnelHost() {
        return tunnelHost;
    }

    public void setTunnelHost(String tunnelHost) {
        this.tunnelHost = tunnelHost;
    }

    public Integer getTunnelPort() {
        return tunnelPort;
    }

    public void setTunnelPort(Integer tunnelPort) {
        this.tunnelPort = tunnelPort;
    }

    public String getTunnelUser() {
        return tunnelUser;
    }

    public void setTunnelUser(String tunnelUser) {
        this.tunnelUser = tunnelUser;
    }

    public String getTunnelUserPassword() {
        return tunnelUserPassword;
    }

    public void setTunnelUserPassword(String tunnelUserPassword) {
        this.tunnelUserPassword = tunnelUserPassword;
    }
}
