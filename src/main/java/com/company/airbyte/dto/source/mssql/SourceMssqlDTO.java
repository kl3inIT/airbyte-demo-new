package com.company.airbyte.dto.source.mssql;

import com.company.airbyte.dto.source.SourceDTO;

public class SourceMssqlDTO extends SourceDTO {

    private String host;
    private Long port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Long getPort() {
        return port;
    }

    public void setPort(Long port) {
        this.port = port;
    }
}
