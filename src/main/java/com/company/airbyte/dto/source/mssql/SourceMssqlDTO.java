package com.company.airbyte.dto.source.mssql;

import com.company.airbyte.dto.source.SourceDatabaseDTO;
import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity
public class SourceMssqlDTO extends SourceDatabaseDTO {

    private String sslMethod;

    private String replicationMethod;

    public SourceMssqlSSLMethodType getSslMethod() {
        return sslMethod == null ? null : SourceMssqlSSLMethodType.fromId(sslMethod);
    }

    public void setSslMethod(SourceMssqlSSLMethodType sslMethod) {
        this.sslMethod = sslMethod == null ? null : sslMethod.getId();
    }

    public SourceMssqlUpdateMethodType getReplicationMethod() {
        return replicationMethod == null ? null : SourceMssqlUpdateMethodType.fromId(replicationMethod);
    }

    public void setReplicationMethod(SourceMssqlUpdateMethodType replicationMethod) {
        this.replicationMethod = replicationMethod == null ? null : replicationMethod.getId();
    }

}
