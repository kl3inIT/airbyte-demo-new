package com.company.airbyte.dto.source.mysql.dto;


import com.company.airbyte.dto.source.SourceDatabaseDTO;
import com.company.airbyte.dto.source.common.SourceSSHTunnelMethod;
import com.company.airbyte.dto.source.mysql.enums.SourceMySQEncryptionModes;
import com.company.airbyte.dto.source.mysql.enums.SourceMySQLUpdateMethod;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.flowui.model.InstanceContainer;

@JmixEntity
public class SourceMysqlDTO extends SourceDatabaseDTO {

    private String encryption;
    private String replicationMethod;
    private Long checkpointInterval;
    private Integer maxQueries;
    private Boolean checkPrivileges;

    private InstanceContainer<com.company.airbyte.dto.source.mysql.dto.SourceMysqlDTO> mysqlDc;

    public SourceMySQLUpdateMethod getReplicationMethod() {
        return replicationMethod == null ? null : SourceMySQLUpdateMethod.fromId(replicationMethod);
    }

    public void setReplicationMethod(SourceMySQLUpdateMethod replicationMethod) {
        this.replicationMethod = replicationMethod == null ? null : replicationMethod.getId();
    }



    public SourceMySQEncryptionModes getEncryption() {
        return encryption == null ? null : SourceMySQEncryptionModes.fromId(encryption);
    }

    public void setEncryption(SourceMySQEncryptionModes encryption) {
        this.encryption = encryption == null ? null : encryption.getId();
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

}