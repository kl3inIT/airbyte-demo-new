package com.company.airbyte.dto.source.postgres;

import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.validation.constraints.NotBlank;

@JmixEntity
public class ReadChangesUsingWriteAheadLogCDCDTO {

    @NotBlank
    private String replicationSlot;

    @NotBlank
    private String publication;

    private Integer initialWaitingSeconds = 1200;

    private Integer queueSize = 10000;

    private String lsnCommitBehaviour;

    private String heartbeatActionQuery;

    private String invalidCdcCursorPositionBehavior;

    private Integer initialLoadTimeoutHours = 8;

    private String plugin;

    public LSNCommitBehaviour getLsnCommitBehaviour() {
        return lsnCommitBehaviour == null ? null : LSNCommitBehaviour.fromId(lsnCommitBehaviour);
    }

    public void setLsnCommitBehaviour(LSNCommitBehaviour lsnCommitBehaviour) {
        this.lsnCommitBehaviour = lsnCommitBehaviour == null ? null : lsnCommitBehaviour.getId();
    }


    public SourcePostgresInvalidCDCPositionBehaviorAdvanced getInvalidCdcCursorPositionBehavior() {
        return invalidCdcCursorPositionBehavior == null ? null : SourcePostgresInvalidCDCPositionBehaviorAdvanced.fromId(invalidCdcCursorPositionBehavior);
    }

    public void setInvalidCdcCursorPositionBehavior(SourcePostgresInvalidCDCPositionBehaviorAdvanced invalidCdcCursorPositionBehavior) {
        this.invalidCdcCursorPositionBehavior = invalidCdcCursorPositionBehavior == null ? null : invalidCdcCursorPositionBehavior.getId();
    }

    public String getReplicationSlot() {
        return replicationSlot;
    }

    public void setReplicationSlot(String replicationSlot) {
        this.replicationSlot = replicationSlot;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public Integer getInitialWaitingSeconds() {
        return initialWaitingSeconds;
    }

    public void setInitialWaitingSeconds(Integer initialWaitingSeconds) {
        this.initialWaitingSeconds = initialWaitingSeconds;
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }

    public String getHeartbeatActionQuery() {
        return heartbeatActionQuery;
    }

    public void setHeartbeatActionQuery(String heartbeatActionQuery) {
        this.heartbeatActionQuery = heartbeatActionQuery;
    }

    public Integer getInitialLoadTimeoutHours() {
        return initialLoadTimeoutHours;
    }

    public void setInitialLoadTimeoutHours(Integer initialLoadTimeoutHours) {
        this.initialLoadTimeoutHours = initialLoadTimeoutHours;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }
}