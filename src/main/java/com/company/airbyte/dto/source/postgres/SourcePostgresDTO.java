package com.company.airbyte.dto.source.postgres;

import com.company.airbyte.dto.source.SourceDatabaseDTO;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.Objects;

@JmixEntity
public class SourcePostgresDTO extends SourceDatabaseDTO {

    private String sslMode;

    private String replicationMethod;

    private SourcePostgresVerifyDTO verifyFullDTO;

    private ReadChangesUsingWriteAheadLogCDCDTO cdcDTO;

    public ReadChangesUsingWriteAheadLogCDCDTO getCdcDTO() {
        return cdcDTO;
    }

    public void setCdcDTO(ReadChangesUsingWriteAheadLogCDCDTO cdcDTO) {
        this.cdcDTO = cdcDTO;
    }

    public SourcePostgresVerifyDTO getVerifyFullDTO() {
        return verifyFullDTO;
    }

    public void setVerifyFullDTO(SourcePostgresVerifyDTO verifyFullDTO) {
        this.verifyFullDTO = verifyFullDTO;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // gọi equals() của SourceDatabaseDTO
        SourcePostgresDTO that = (SourcePostgresDTO) o;
        return Objects.equals(sslMode, that.sslMode) &&
                Objects.equals(replicationMethod, that.replicationMethod) &&
                Objects.equals(verifyFullDTO, that.verifyFullDTO) &&
                Objects.equals(cdcDTO, that.cdcDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sslMode, replicationMethod, verifyFullDTO, cdcDTO);
    }


}
