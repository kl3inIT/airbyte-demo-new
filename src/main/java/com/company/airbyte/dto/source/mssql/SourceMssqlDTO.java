package com.company.airbyte.dto.source.mssql;

import com.company.airbyte.dto.source.SourceDTO;
import com.company.airbyte.dto.source.SourceDatabaseDTO;
import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity
public class SourceMssqlDTO extends SourceDatabaseDTO {

    private String sslMethod;

    private String replicationMethod;

    public SourceMssqlSSLMethod getSslMethod() {
        return sslMethod == null ? null : SourceMssqlSSLMethod.fromId(sslMethod);
    }

    public void setSslMethod(SourceMssqlSSLMethod sslMethod) {
        this.sslMethod = sslMethod == null ? null : sslMethod.getId();
    }

    public SourceMssqlUpdateMethod getReplicationMethod() {
        return replicationMethod == null ? null : SourceMssqlUpdateMethod.fromId(replicationMethod);
    }

    public void setReplicationMethod(SourceMssqlUpdateMethod replicationMethod) {
        this.replicationMethod = replicationMethod == null ? null : replicationMethod.getId();
    }

}
