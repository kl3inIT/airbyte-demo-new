package com.company.airbyte.entity;

import com.company.airbyte.converter.source.SourceDTOConverter;
import com.company.airbyte.dto.source.SourceDTO;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@JmixEntity
@Table(name = "SOURCE")
@Entity
public class Source extends BaseEntity {
    @Column(name = "SOURCE_ID", nullable = false)
    @NotNull
    private UUID sourceID;

    @Column(name = "DEFINITION_ID", nullable = false)
    @NotNull
    private UUID definitionId;

    @Column(name = "WORKSPACE_ID", nullable = false)
    @NotNull
    private UUID workspaceId;

    @Column(name = "SOURCE_TYPE", nullable = false)
    @NotNull
    private String sourceType;

    @Column(name = "PROVIDER_UNIT", nullable = false)
    @NotNull
    private String providerUnit;

    @Column(name = "DATA_FORMAT", nullable = false)
    @NotNull
    private String dataFormat;

    @InstanceName
    @Column(name = "DESCRIPTION", length = 200)
    private String description;

    @NotNull
    @Column(name = "CONFIGURATION", nullable = false, columnDefinition = "jsonb")
    @Convert(converter = SourceDTOConverter.class)
    private SourceDTO configuration;

    public DataFormat getDataFormat() {
        return dataFormat == null ? null : DataFormat.fromId(dataFormat);
    }

    public void setDataFormat(DataFormat dataFormat) {
        this.dataFormat = dataFormat == null ? null : dataFormat.getId();
    }

    public UUID getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(UUID workspaceId) {
        this.workspaceId = workspaceId;
    }

    public UUID getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(UUID definitionId) {
        this.definitionId = definitionId;
    }

    public UUID getSourceID() {
        return sourceID;
    }

    public void setSourceID(UUID sourceID) {
        this.sourceID = sourceID;
    }

    public SourceDTO getConfiguration() {
        return configuration;
    }

    public void setConfiguration(SourceDTO configuration) {
        this.configuration = configuration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProviderUnit getProviderUnit() {
        return providerUnit == null ? null : ProviderUnit.fromId(providerUnit);
    }

    public void setProviderUnit(ProviderUnit providerUnit) {
        this.providerUnit = providerUnit == null ? null : providerUnit.getId();
    }

    public SourceType getSourceType() {
        return sourceType == null ? null : SourceType.fromId(sourceType);
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType == null ? null : sourceType.getId();
    }

}