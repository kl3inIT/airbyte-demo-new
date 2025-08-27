package com.company.airbyte.dto.source.postgres;

import com.company.airbyte.dto.source.SourceDatabaseDTO;
import com.company.airbyte.entity.DatabaseType;
import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity
public class SourcePostgresDTO extends SourceDatabaseDTO {

    private String sslMode;

    private String replicationMethod;

    public SourcePostgresUpdateMethod getReplicationMethod() {
        return replicationMethod == null ? null : SourcePostgresUpdateMethod.fromId(replicationMethod);
    }

    public void setReplicationMethod(SourcePostgresUpdateMethod replicationMethod) {
        this.replicationMethod = replicationMethod == null ? null : replicationMethod.getId();
    }

    public SourcePostgresSSLModes getSslMode() {
        return sslMode == null ? null : SourcePostgresSSLModes.fromId(sslMode);
    }

    public void setSslMode(SourcePostgresSSLModes sslMode) {
        this.sslMode = sslMode == null ? null : sslMode.getId();
    }

}
