package com.company.airbyte.dto.source.mysql.dto;


import com.company.airbyte.dto.source.common.SourceSSHTunnelMethod;
import com.company.airbyte.dto.source.mysql.enums.SourceMySQEncryptionModes;
import com.company.airbyte.dto.source.mysql.enums.SourceMySQLUpdateMethod;
import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity
public class SourceMysqlDTO {

    private String host;
    private Long port;
    private String username;
    private String password;
    private String database;
    private String encryption;
    private String tunnelMethod;
    private String replicationMethod;
    private String jdbcUrlParams;
        private Long checkpointInterval;
    private Integer maxQueries;
    private Boolean checkPrivileges;


    public SourceMySQLUpdateMethod getReplicationMethod() {
        return replicationMethod == null ? null : SourceMySQLUpdateMethod.fromId(replicationMethod);
    }

    public void setReplicationMethod(SourceMySQLUpdateMethod replicationMethod) {
        this.replicationMethod = replicationMethod == null ? null : replicationMethod.getId();
    }

    public SourceSSHTunnelMethod getTunnelMethod() {
        return tunnelMethod == null ? null : SourceSSHTunnelMethod.fromId(tunnelMethod);
    }

    public void setTunnelMethod(SourceSSHTunnelMethod tunnelMethod) {
        this.tunnelMethod = tunnelMethod == null ? null : tunnelMethod.getId();
    }

    public SourceMySQEncryptionModes getEncryption() {
        return encryption == null ? null : SourceMySQEncryptionModes.fromId(encryption);
    }

    public void setEncryption(SourceMySQEncryptionModes encryption) {
        this.encryption = encryption == null ? null : encryption.getId();
    }

    public String getJdbcUrlParams() {
        return jdbcUrlParams;
    }

    public void setJdbcUrlParams(String jdbcUrlParams) {
        this.jdbcUrlParams = jdbcUrlParams;
    }

    public Long getCheckpointInterval() {
        return checkpointInterval;
    }

    public void setCheckpointInterval(Long checkpointInterval) {
        this.checkpointInterval = checkpointInterval;
    }

    public Integer getMaxQueries() {
        return maxQueries;
    }

    public void setMaxQueries(Integer maxQueries) {
        this.maxQueries = maxQueries;
    }

    public Boolean getCheckPrivileges() {
        return checkPrivileges;
    }

    public void setCheckPrivileges(Boolean checkPrivileges) {
        this.checkPrivileges = checkPrivileges;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getPort() {
        return port;
    }

    public void setPort(Long port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}