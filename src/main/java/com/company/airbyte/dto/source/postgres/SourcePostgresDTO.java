package com.company.airbyte.dto.source.postgres;

import com.company.airbyte.dto.source.SourceDTO;
import com.company.airbyte.dto.source.common.SourceSSHTunnelMethod;
import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity
public class SourcePostgresDTO extends SourceDTO {

    private String host;

    private Long port;

    private String database;

    private String schemas;

    private String username;

    private String password;

    private String sslMode;

    private String tunnelMethod;

    private String replicationMethod;

    private String sourceType;

    private String jdbcUrlParams;

    public SourcePostgresUpdateMethod getReplicationMethod() {
        return replicationMethod == null ? null : SourcePostgresUpdateMethod.fromId(replicationMethod);
    }

    public void setReplicationMethod(SourcePostgresUpdateMethod replicationMethod) {
        this.replicationMethod = replicationMethod == null ? null : replicationMethod.getId();
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public SourcePostgresSSLModes getSslMode() {
        return sslMode == null ? null : SourcePostgresSSLModes.fromId(sslMode);
    }

    public void setSslMode(SourcePostgresSSLModes sslMode) {
        this.sslMode = sslMode == null ? null : sslMode.getId();
    }

    public SourceSSHTunnelMethod getTunnelMethod() {
        return tunnelMethod == null ? null : SourceSSHTunnelMethod.fromId(tunnelMethod);
    }

    public void setTunnelMethod(SourceSSHTunnelMethod tunnelMethod) {
        this.tunnelMethod = tunnelMethod == null ? null : tunnelMethod.getId();
    }

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

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getSchemas() {
        return schemas;
    }

    public void setSchemas(String schemas) {
        this.schemas = schemas;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJdbcUrlParams() {
        return jdbcUrlParams;
    }

    public void setJdbcUrlParams(String jdbcUrlParams) {
        this.jdbcUrlParams = jdbcUrlParams;
    }
}
