package com.company.airbyte.dto.source.postgres;

import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity
public class ReadChangesUsingWriteAheadLogCDCDTO {

    private String replicationSlot;

    private String publication;

    private Long initialWaitingSeconds;

    private Long queueSize;

    private String lsnCommitBehaviour;

    private String heartbeatActionQuery;

    private String invalidCdcCursorPositionBehavior;

    private Long initialLoadTimeoutHours;

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

    public Long getInitialWaitingSeconds() {
        return initialWaitingSeconds;
    }

    public void setInitialWaitingSeconds(Long initialWaitingSeconds) {
        this.initialWaitingSeconds = initialWaitingSeconds;
    }

    public Long getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Long queueSize) {
        this.queueSize = queueSize;
    }

    public String getHeartbeatActionQuery() {
        return heartbeatActionQuery;
    }

    public void setHeartbeatActionQuery(String heartbeatActionQuery) {
        this.heartbeatActionQuery = heartbeatActionQuery;
    }

    public Long getInitialLoadTimeoutHours() {
        return initialLoadTimeoutHours;
    }

    public void setInitialLoadTimeoutHours(Long initialLoadTimeoutHours) {
        this.initialLoadTimeoutHours = initialLoadTimeoutHours;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }
}