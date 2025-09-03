package com.company.airbyte.dto.source;

import com.company.airbyte.dto.source.common.SourceSSHTunnelMethod;
import com.company.airbyte.dto.source.common.SourceSSHTunnelMethodDTO;
import com.company.airbyte.entity.DatabaseType;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

@JmixEntity
public class SourceDatabaseDTO extends SourceDTO {

    private String databaseType;

    @NotBlank
    private String host;

    @NotNull
    private Long port;

    @NotBlank
    private String database;

    private String schemas;

    @NotBlank
    private String username;

    private String password;

    private String tunnelMethod;

    private String jdbcUrlParams;

    private SourceSSHTunnelMethodDTO sshTunnelMethod;

    public SourceSSHTunnelMethodDTO getSshTunnelMethod() {
        return sshTunnelMethod;
    }

    public void setSshTunnelMethod(SourceSSHTunnelMethodDTO sshTunnelMethod) {
        this.sshTunnelMethod = sshTunnelMethod;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // gọi equals() của SourceDTO
        SourceDatabaseDTO that = (SourceDatabaseDTO) o;
        return Objects.equals(databaseType, that.databaseType) &&
                Objects.equals(host, that.host) &&
                Objects.equals(port, that.port) &&
                Objects.equals(database, that.database) &&
                Objects.equals(username, that.username) &&
                Objects.equals(schemas, that.schemas) &&
                Objects.equals(tunnelMethod, that.tunnelMethod) &&
                Objects.equals(jdbcUrlParams, that.jdbcUrlParams) &&
                Objects.equals(sshTunnelMethod, that.sshTunnelMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                databaseType, host, port, database, username,
                schemas, tunnelMethod, jdbcUrlParams, sshTunnelMethod
        );
    }


}
