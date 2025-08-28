package com.company.airbyte.dto.source.mysql.dto;


import com.company.airbyte.dto.source.postgres.SourcePostgresInvalidCDCPositionBehaviorAdvanced;
import io.jmix.core.metamodel.annotation.JmixEntity;


@JmixEntity
public class MysqlBinlogCDCDTO {


    private String serverTimezone;
    private String invalidCdcCursorPositionBehavior;
    private Long initialLoadTimeoutHours;


    public SourcePostgresInvalidCDCPositionBehaviorAdvanced getInvalidCdcCursorPositionBehavior() {
        return invalidCdcCursorPositionBehavior == null ? null : SourcePostgresInvalidCDCPositionBehaviorAdvanced.fromId(invalidCdcCursorPositionBehavior);
    }

    public void setInvalidCdcCursorPositionBehavior(SourcePostgresInvalidCDCPositionBehaviorAdvanced invalidCdcCursorPositionBehavior) {
        this.invalidCdcCursorPositionBehavior = invalidCdcCursorPositionBehavior == null ? null : invalidCdcCursorPositionBehavior.getId();
    }


    public Long getInitialLoadTimeoutHours() {
        return initialLoadTimeoutHours;
    }

    public void setInitialLoadTimeoutHours(Long initialLoadTimeoutHours) {
        this.initialLoadTimeoutHours = initialLoadTimeoutHours;
    }

    public String getServerTimezone() {
        return serverTimezone;
    }

    public void setServerTimezone(String serverTimezone) {
        this.serverTimezone = serverTimezone;
    }


}