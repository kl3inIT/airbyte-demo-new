package com.company.airbyte.service;

import com.airbyte.api.Airbyte;
import com.airbyte.api.models.operations.CreateSourceResponse;
import com.airbyte.api.models.operations.PutSourceRequest;
import com.airbyte.api.models.operations.PutSourceResponse;
import com.airbyte.api.models.shared.*;
import com.company.airbyte.dto.source.SourceDatabaseDTO;
import com.company.airbyte.dto.source.common.PasswordAuthenticationDTO;
import com.company.airbyte.dto.source.common.SSHKeyAuthenticationDTO;
import com.company.airbyte.dto.source.common.SourceSSHTunnelMethod;
import com.company.airbyte.dto.source.postgres.*;
import com.company.airbyte.entity.DatabaseType;
import com.company.airbyte.entity.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AirbyteService {

    private static final Logger logger = LoggerFactory.getLogger(AirbyteService.class);

    private final Airbyte airbyteClient;

    @Value("${airbyte.workspace.id}")
    private String workspaceId;

    public AirbyteService(Airbyte airbyteClient) {
        this.airbyteClient = airbyteClient;
    }

    public SourceResponse upsertSourceOnAirbyte(Source sourceEntity) {
        Objects.requireNonNull(sourceEntity, "Source entity cannot be null");

        if (!(sourceEntity.getConfiguration() instanceof SourceDatabaseDTO sourceConfig)) {
            throw new IllegalArgumentException("Unsupported configuration type: " +
                    (sourceEntity.getConfiguration() == null ? "null" : sourceEntity.getConfiguration().getClass().getSimpleName()));
        }

        DatabaseType databaseType = sourceConfig.getDatabaseType();
        if (databaseType == null) {
            throw new IllegalArgumentException("Database type is required");
        }

        SourceConfiguration airbyteConfiguration = buildAirbyteConfiguration(sourceConfig, databaseType);

        if (sourceEntity.getSourceID() == null) {
            return createSourceOnAirbyte(sourceEntity, airbyteConfiguration);
        } else {
            return updateSourceOnAirbyte(sourceEntity, airbyteConfiguration);
        }
    }


    private SourceResponse createSourceOnAirbyte(Source sourceEntity, SourceConfiguration airbyteConfiguration) {
        SourceCreateRequest createRequest = SourceCreateRequest.builder()
                .name(sourceEntity.getName())
                .workspaceId(workspaceId)
                .configuration(airbyteConfiguration)
                .build();
        try {
            CreateSourceResponse createResponse = airbyteClient.sources()
                    .createSource()
                    .request(createRequest)
                    .call();

            return createResponse.sourceResponse()
                    .orElseThrow(() -> new IllegalStateException("Empty response"));
        } catch (Exception e) {
            logger.error("Airbyte createSource failed", e);
            throw new IllegalStateException("Airbyte createSource failed: " + e.getMessage(), e);
        }
    }


    private SourceResponse updateSourceOnAirbyte(Source sourceEntity, SourceConfiguration configuration) {
        PutSourceRequest req = PutSourceRequest.builder()
                .sourceId(sourceEntity.getSourceID().toString())
                .sourcePutRequest(SourcePutRequest.builder()
                        .name(sourceEntity.getName())
                        .configuration(configuration)
                        .build())
                .build();
        try {
            PutSourceResponse res = airbyteClient.sources()
                    .putSource()
                    .request(req)
                    .call();

            return res.sourceResponse()
                    .orElseThrow(() -> new IllegalStateException("Airbyte putSource returned empty response"));
        } catch (Exception e) {
            logger.error("Airbyte putSource failed", e);
            throw new IllegalStateException("Airbyte putSource failed: " + e.getMessage(), e);
        }
    }


    private SourceConfiguration buildAirbyteConfiguration(SourceDatabaseDTO sourceConfig, DatabaseType databaseType) {
        switch (databaseType) {
            case POSTGRES:
                return SourceConfiguration.of(buildPostgresConfiguration((SourcePostgresDTO) sourceConfig));
            case MSSQL:
            case MYSQL:
                logger.warn("Database type {} is not yet supported, returning null", databaseType);
                return null;
            default:
                throw new IllegalArgumentException("Unsupported database type: " + databaseType);
        }
    }


    private SourcePostgres buildPostgresConfiguration(SourcePostgresDTO postgresConfig) {
        SourcePostgres.Builder postgresBuilder = SourcePostgres.builder()
                .database(validateRequiredField(postgresConfig.getDatabase(), "database"))
                .host(validateRequiredField(postgresConfig.getHost(), "host"))
                .username(validateRequiredField(postgresConfig.getUsername(), "username"))
                .jdbcUrlParams(Optional.ofNullable(postgresConfig.getJdbcUrlParams()))
                .password(Optional.ofNullable(postgresConfig.getPassword()))
                .port(Optional.ofNullable(postgresConfig.getPort()));

        configureReplicationMethod(postgresBuilder, postgresConfig);
        configureSslMode(postgresBuilder, postgresConfig);
        configureTunnelMethod(postgresBuilder, postgresConfig);
        configureSchemas(postgresBuilder, postgresConfig);

        return postgresBuilder.build();
    }


    private void configureReplicationMethod(SourcePostgres.Builder postgresBuilder, SourcePostgresDTO postgresConfig) {
        SourcePostgresUpdateMethodType replicationMethod = postgresConfig.getReplicationMethod();
        ReadChangesUsingWriteAheadLogCDCDTO cdcConfiguration = postgresConfig.getCdcDTO();

        switch (replicationMethod) {
            case CDC:
                ReadChangesUsingWriteAheadLogCDC.Builder cdcBuilder = buildCdcConfiguration(cdcConfiguration);
                postgresBuilder.replicationMethod(Optional.of(SourcePostgresUpdateMethod.of(cdcBuilder.build())));
                break;
            case XMIN:
                postgresBuilder.replicationMethod(Optional.of(SourcePostgresUpdateMethod.of(
                        DetectChangesWithXminSystemColumn.builder().build()
                )));
                break;
            case STANDARD:
                postgresBuilder.replicationMethod(Optional.of(SourcePostgresUpdateMethod.of(
                        SourcePostgresScanChangesWithUserDefinedCursor.builder().build()
                )));
                break;
            default:
                logger.warn("Unknown replication method: {}, using XMIN as default", replicationMethod);
                break;
        }
    }


    private ReadChangesUsingWriteAheadLogCDC.Builder buildCdcConfiguration(ReadChangesUsingWriteAheadLogCDCDTO cdcConfig) {
        ReadChangesUsingWriteAheadLogCDC.Builder cdcBuilder = ReadChangesUsingWriteAheadLogCDC.builder()
                .replicationSlot(validateRequiredField(cdcConfig.getReplicationSlot(), "replicationSlot"))
                .publication(validateRequiredField(cdcConfig.getPublication(), "publication"))
                .initialWaitingSeconds(Optional.ofNullable(cdcConfig.getInitialWaitingSeconds()))
                .queueSize(Optional.ofNullable(cdcConfig.getQueueSize()))
                .initialLoadTimeoutHours(Optional.ofNullable(cdcConfig.getInitialLoadTimeoutHours()))
                .heartbeatActionQuery(Optional.ofNullable(cdcConfig.getHeartbeatActionQuery()));

        configureLsnCommitBehaviour(cdcBuilder, cdcConfig);
        configureInvalidCdcPositionBehavior(cdcBuilder, cdcConfig);

        return cdcBuilder;
    }


    private void configureLsnCommitBehaviour(ReadChangesUsingWriteAheadLogCDC.Builder cdcBuilder,
                                             ReadChangesUsingWriteAheadLogCDCDTO cdcConfig) {
        if (cdcConfig.getLsnCommitBehaviour() == LSNCommitBehaviourType.WHILE_READING_DATA) {
            cdcBuilder.lsnCommitBehaviour(Optional.of(LSNCommitBehaviour.WHILE_READING_DATA));
        } else {
            // Default: AFTER_LOADING_DATA
            cdcBuilder.lsnCommitBehaviour(Optional.of(LSNCommitBehaviour.AFTER_LOADING_DATA_IN_THE_DESTINATION));
        }
    }


    private void configureInvalidCdcPositionBehavior(ReadChangesUsingWriteAheadLogCDC.Builder cdcBuilder,
                                                     ReadChangesUsingWriteAheadLogCDCDTO cdcConfig) {
        if (cdcConfig.getInvalidCdcCursorPositionBehavior() == SourcePostgresInvalidCDCPositionBehaviorAdvancedType.RE_SYNC_DATA) {
            cdcBuilder.invalidCdcCursorPositionBehavior(Optional.of(
                    SourcePostgresInvalidCDCPositionBehaviorAdvanced.RE_SYNC_DATA));
        } else {
            // Default: FAIL_SYNC
            cdcBuilder.invalidCdcCursorPositionBehavior(Optional.of(
                    SourcePostgresInvalidCDCPositionBehaviorAdvanced.FAIL_SYNC));

        }
    }


    private void configureSslMode(SourcePostgres.Builder postgresBuilder, SourcePostgresDTO postgresConfig) {
        SourcePostgresSSLModesType sslMode = postgresConfig.getSslMode();

        if (sslMode == SourcePostgresSSLModesType.VERIFY_CA || sslMode == SourcePostgresSSLModesType.VERIFY_FULL) {
            configureVerifySslMode(postgresBuilder, postgresConfig);
        } else {
            configureStandardSslMode(postgresBuilder, sslMode);
        }
    }


    private void configureVerifySslMode(SourcePostgres.Builder postgresBuilder, SourcePostgresDTO postgresConfig) {
        SourcePostgresSSLModesType sslMode = postgresConfig.getSslMode();
        SourcePostgresVerifyDTO verifyConfig = postgresConfig.getVerifyFullDTO();

        if (sslMode == SourcePostgresSSLModesType.VERIFY_CA) {
            // VERIFY_CA: chỉ cần CA certificate
            SourcePostgresVerifyCa.Builder sslBuilder = SourcePostgresVerifyCa.builder()
                    .caCertificate(validateRequiredField(verifyConfig.getCaCertificate(), "caCertificate"));

            postgresBuilder.sslMode(Optional.of(SourcePostgresSSLModes.of(sslBuilder.build())));
        } else {
            // VERIFY_FULL: cần CA certificate + client certificate + client key
            SourcePostgresVerifyFull.Builder sslBuilder = SourcePostgresVerifyFull.builder()
                    .caCertificate(validateRequiredField(verifyConfig.getCaCertificate(), "caCertificate"));

            if (verifyConfig.getClientCertificate() != null) {
                sslBuilder.clientCertificate(Optional.of(verifyConfig.getClientCertificate()));
            }
            if (verifyConfig.getClientKey() != null) {
                sslBuilder.clientKey(Optional.of(verifyConfig.getClientKey()));
            }
            if (verifyConfig.getClientKeyPassword() != null) {
                sslBuilder.clientKeyPassword(Optional.of(verifyConfig.getClientKeyPassword()));
            }

            postgresBuilder.sslMode(Optional.of(SourcePostgresSSLModes.of(sslBuilder.build())));
        }
    }


    private void configureStandardSslMode(SourcePostgres.Builder postgresBuilder, SourcePostgresSSLModesType sslMode) {
        switch (sslMode) {
            case DISABLE:
                postgresBuilder.sslMode(Optional.of(SourcePostgresSSLModes.of(
                        SourcePostgresDisable.builder().build()
                )));
                break;
            case ALLOW:
                postgresBuilder.sslMode(Optional.of(SourcePostgresSSLModes.of(
                        SourcePostgresAllow.builder().build()
                )));
                break;
            case PREFER:
                postgresBuilder.sslMode(Optional.of(SourcePostgresSSLModes.of(
                        SourcePostgresPrefer.builder().build()
                )));
                break;
            case REQUIRE:
                postgresBuilder.sslMode(Optional.of(SourcePostgresSSLModes.of(
                        SourcePostgresRequire.builder().build()
                )));
                break;
            default:
                logger.warn("Unknown SSL mode: {}, using DISABLE as default", sslMode);
                postgresBuilder.sslMode(Optional.of(SourcePostgresSSLModes.of(
                        SourcePostgresDisable.builder().build()
                )));
                break;
        }
    }

    private void configureTunnelMethod(SourcePostgres.Builder postgresBuilder, SourcePostgresDTO postgresConfig) {
        SourceSSHTunnelMethod tunnelMethod = postgresConfig.getTunnelMethod();

        if (tunnelMethod == null) {
            // Default: NO_TUNNEL
            postgresBuilder.tunnelMethod(Optional.of(SourcePostgresSSHTunnelMethod.of(
                    SourcePostgresNoTunnel.builder().build()
            )));
            return;
        }

        switch (tunnelMethod) {
            case NO_TUNNEL:
                postgresBuilder.tunnelMethod(Optional.of(SourcePostgresSSHTunnelMethod.of(
                        SourcePostgresNoTunnel.builder().build()
                )));
                break;

            case SSH_KEY_AUTH:
                SSHKeyAuthenticationDTO sshKeyConfig = (SSHKeyAuthenticationDTO) postgresConfig.getSshTunnelMethod();
                SourcePostgresSSHKeyAuthentication.Builder sshKeyBuilder = SourcePostgresSSHKeyAuthentication.builder()
                        .sshKey(validateRequiredField(sshKeyConfig.getSshKey(), "sshKey"))
                        .tunnelHost(validateRequiredField(sshKeyConfig.getTunnelHost(), "tunnelHost"))
                        .tunnelUser(validateRequiredField(sshKeyConfig.getTunnelUser(), "tunnelUser"));

                if (sshKeyConfig.getTunnelPort() != null) {
                    sshKeyBuilder.tunnelPort(Optional.of(sshKeyConfig.getTunnelPort()));
                }

                postgresBuilder.tunnelMethod(Optional.of(SourcePostgresSSHTunnelMethod.of(sshKeyBuilder.build())));
                break;

            case SSH_PASSWORD_AUTH:
                PasswordAuthenticationDTO passwordAuthConfig = (PasswordAuthenticationDTO) postgresConfig.getSshTunnelMethod();
                SourcePostgresPasswordAuthentication.Builder passwordAuthBuilder = SourcePostgresPasswordAuthentication.builder()
                        .tunnelHost(validateRequiredField(passwordAuthConfig.getTunnelHost(), "tunnelHost"))
                        .tunnelUser(validateRequiredField(passwordAuthConfig.getTunnelUser(), "tunnelUser"))
                        .tunnelUserPassword(validateRequiredField(passwordAuthConfig.getTunnelUserPassword(), "tunnelUserPassword"));

                if (passwordAuthConfig.getTunnelPort() != null) {
                    passwordAuthBuilder.tunnelPort(Optional.of(passwordAuthConfig.getTunnelPort()));
                }

                postgresBuilder.tunnelMethod(Optional.of(SourcePostgresSSHTunnelMethod.of(passwordAuthBuilder.build())));
                break;

            default:
                logger.warn("Unknown tunnel method: {}, using NO_TUNNEL as default", tunnelMethod);

                break;
        }
    }

    private void configureSchemas(SourcePostgres.Builder postgresBuilder, SourcePostgresDTO postgresConfig) {
        if (postgresConfig.getSchemas() != null && !postgresConfig.getSchemas().isBlank()) {
            postgresBuilder.schemas(Optional.of(List.of(postgresConfig.getSchemas())));
        } else {
            postgresBuilder.schemas(Optional.empty());
        }
    }


    private String validateRequiredField(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
        return value;
    }
}
