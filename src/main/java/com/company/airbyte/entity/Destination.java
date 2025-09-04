package com.company.airbyte.entity;

import com.company.airbyte.converter.destination.DestinationDTOConverter;
import com.company.airbyte.dto.destination.DestinationDTO;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@JmixEntity
@Table(name = "DESTINATION")
@Entity
public class Destination extends BaseEntity {

    @Column(name = "DESTINATION_ID")
    private UUID destinationId;

    @Column(name = "DEFINITION_ID")
    private UUID definitionId;

    @Column(name = "WORKSPACE_ID")
    private UUID workspaceId;

    @Column(name = "DESTINATION_TYPE")
    private String destinationType;

    @Column(name = "DATA_FORMAT")
    private String dataFormat;

    @Column(name = "DESCRIPTION", length = 200)
    private String description;

    @Column(name = "CONFIGURATION", columnDefinition = "jsonb")
    @Convert(converter = DestinationDTOConverter.class)
    private DestinationDTO configuration;


    public UUID getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(UUID destinationId) {
        this.destinationId = destinationId;
    }

    public UUID getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(UUID definitionId) {
        this.definitionId = definitionId;
    }

    public UUID getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(UUID workspaceId) {
        this.workspaceId = workspaceId;
    }

    public DestinationType getDestinationType() {
        return destinationType == null ? null : DestinationType.fromId(destinationType);
    }

    public void setDestinationType(DestinationType type) {
        this.destinationType = type == null ? null : type.getId();
    }

    public DataFormat getDataFormat() {
        return dataFormat == null ? null : DataFormat.fromId(dataFormat);
    }

    public void setDataFormat(DataFormat format) {
        this.dataFormat = format == null ? null : format.getId();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DestinationDTO getConfiguration() {
        return configuration;
    }

    public void setConfiguration(DestinationDTO configuration) {
        this.configuration = configuration;
    }
}
