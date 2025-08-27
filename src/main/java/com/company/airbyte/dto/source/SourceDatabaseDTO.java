package com.company.airbyte.dto.source;

import com.company.airbyte.dto.source.common.SourceSSHTunnelMethod;
import com.company.airbyte.entity.DatabaseType;
import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity
public class SourceDatabaseDTO extends SourceDTO {

    private String databaseType;

    private String host;

    private Long port;

    private String database;

    private String schemas;

    private String username;

    private String password;

    private String tunnelMethod;

    private String jdbcUrlParams;

    public DatabaseType getDatabaseType() {
        return databaseType == null ? null : DatabaseType.fromId(databaseType);
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType == null ? null : databaseType.getId();
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSchemas() {
        return schemas;
    }

    public void setSchemas(String schemas) {
        this.schemas = schemas;
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
