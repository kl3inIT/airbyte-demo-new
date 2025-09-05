package com.company.airbyte.service;

import com.airbyte.api.Airbyte;
import com.airbyte.api.models.errors.SDKError;
import com.airbyte.api.models.operations.CreateSourceResponse;
import com.airbyte.api.models.operations.PutSourceRequest;
import com.airbyte.api.models.operations.PutSourceResponse;
import com.airbyte.api.models.shared.*;
import com.company.airbyte.dto.source.SourceDatabaseDTO;
import com.company.airbyte.dto.source.common.PasswordAuthenticationDTO;
import com.company.airbyte.dto.source.common.SSHKeyAuthenticationDTO;
import com.company.airbyte.dto.source.common.SourceSSHTunnelMethod;
import com.company.airbyte.dto.source.file.*;
import com.company.airbyte.dto.source.postgres.*;
import com.company.airbyte.entity.DatabaseType;
import com.company.airbyte.entity.Source;
import com.company.airbyte.entity.SourceType;
import com.company.airbyte.utils.AirbyteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

        SourceType sourceType = sourceEntity.getSourceType();
        if (sourceType == null) {
            throw new IllegalArgumentException("Source type is required");
        }

        SourceConfiguration airbyteConfiguration;

        switch (sourceType) {
            case DATABASE:
                if (!(sourceEntity.getConfiguration() instanceof SourceDatabaseDTO sourceConfig)) {
                    throw new IllegalArgumentException("DATABASE source requires SourceDatabaseDTO configuration");
                }

                DatabaseType databaseType = sourceConfig.getDatabaseType();
                if (databaseType == null) {
                    throw new IllegalArgumentException("Database type is required");
                }

                airbyteConfiguration = buildAirbyteConfiguration(sourceConfig, databaseType);
                break;

            case API:
                // TODO: Support API source type
                logger.warn("API source type is not yet supported");
                throw new UnsupportedOperationException("API source type is not yet supported");

            case FILE:
                if (!(sourceEntity.getConfiguration() instanceof SourceFileDTO sourceConfig)) {
                    throw new IllegalArgumentException("FILE source requires SourceFileDTO configuration");
                }

                airbyteConfiguration = buildFileConfiguration(sourceConfig);
                break;

            default:
                throw new IllegalArgumentException("Unsupported source type: " + sourceType);
        }

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

        // (Tùy chọn) log payload để debug
        logger.debug("CreateSourceRequest payload: {}", createRequest);

        try {
            CreateSourceResponse createResponse = airbyteClient.sources()
                    .createSource()
                    .request(createRequest)
                    .call();

            return createResponse.sourceResponse()
                    .orElseThrow(() -> new IllegalStateException("Empty response"));
        } catch (SDKError e) {
            String bodyText = AirbyteUtils.decodeBody(e);
            logger.error("Airbyte createSource failed: status={} message='{}' body={}",
                    e.code(), e.message(), bodyText);
            throw new IllegalStateException(
                    "Airbyte createSource failed. status=" + e.code() +
                            ", message=" + e.message() +
                            ", body=" + bodyText, e);
        } catch (Exception e) {
            logger.error("Airbyte createSource failed (unexpected)", e);
            throw new IllegalStateException("Airbyte createSource failed: " + String.valueOf(e.getMessage()), e);
        }
    }

    private SourceResponse updateSourceOnAirbyte(Source sourceEntity, SourceConfiguration configuration) {
        PutSourceRequest request = PutSourceRequest.builder()
                .sourceId(sourceEntity.getSourceID().toString())
                .sourcePutRequest(SourcePutRequest.builder()
                        .name(sourceEntity.getName())
                        .configuration(configuration)
                        .build())
                .build();

        // (Tùy chọn) log payload để debug
        logger.debug("PutSourceRequest payload: {}", request);

        try {
            PutSourceResponse response = airbyteClient.sources()
                    .putSource()
                    .request(request)
                    .call();

            return response.sourceResponse()
                    .orElseThrow(() -> new IllegalStateException("Airbyte putSource returned empty response"));
        } catch (SDKError e) {
            String bodyText = AirbyteUtils.decodeBody(e);
            logger.error("Airbyte putSource failed: status={} message='{}' body={}",
                    e.code(), e.message(), bodyText);
            throw new IllegalStateException(
                    "Airbyte putSource failed. status=" + e.code() +
                            ", message=" + e.message() +
                            ", body=" + bodyText, e);
        } catch (Exception e) {
            logger.error("Airbyte putSource failed (unexpected)", e);
            throw new IllegalStateException("Airbyte putSource failed: " + String.valueOf(e.getMessage()), e);
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
                .jdbcUrlParams(Optional.ofNullable((postgresConfig.getJdbcUrlParams())))
                .password(Optional.ofNullable((postgresConfig.getPassword())))
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

        if (replicationMethod == null) {
            logger.warn("Replication method is null; using XMIN as default");
            postgresBuilder.replicationMethod(Optional.of(SourcePostgresUpdateMethod.of(
                    DetectChangesWithXminSystemColumn.builder().build()
            )));
            return;
        }

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
                postgresBuilder.replicationMethod(Optional.of(SourcePostgresUpdateMethod.of(
                        DetectChangesWithXminSystemColumn.builder().build()
                )));
                break;
        }
    }


    private ReadChangesUsingWriteAheadLogCDC.Builder buildCdcConfiguration(ReadChangesUsingWriteAheadLogCDCDTO cdcConfig) {
        Objects.requireNonNull(cdcConfig, "cdc config cannot be null for CDC mode");

        ReadChangesUsingWriteAheadLogCDC.Builder cdcBuilder = ReadChangesUsingWriteAheadLogCDC.builder()
                .replicationSlot(validateRequiredField(cdcConfig.getReplicationSlot(), "replicationSlot"))
                .publication(validateRequiredField(cdcConfig.getPublication(), "publication"))
                .initialWaitingSeconds(Optional.ofNullable(cdcConfig.getInitialWaitingSeconds()))
                .queueSize(Optional.ofNullable(cdcConfig.getQueueSize()))
                .initialLoadTimeoutHours(Optional.ofNullable(cdcConfig.getInitialLoadTimeoutHours()))
                .heartbeatActionQuery(Optional.ofNullable((cdcConfig.getHeartbeatActionQuery())));

        if (cdcConfig.getLsnCommitBehaviour() == LSNCommitBehaviourType.WHILE_READING_DATA) {
            cdcBuilder.lsnCommitBehaviour(Optional.of(LSNCommitBehaviour.WHILE_READING_DATA));
        } else {
            cdcBuilder.lsnCommitBehaviour(Optional.of(LSNCommitBehaviour.AFTER_LOADING_DATA_IN_THE_DESTINATION));
        }

        if (cdcConfig.getInvalidCdcCursorPositionBehavior()
                == SourcePostgresInvalidCDCPositionBehaviorAdvancedType.RE_SYNC_DATA) {
            cdcBuilder.invalidCdcCursorPositionBehavior(Optional.of(
                    SourcePostgresInvalidCDCPositionBehaviorAdvanced.RE_SYNC_DATA));
        } else {
            cdcBuilder.invalidCdcCursorPositionBehavior(Optional.of(
                    SourcePostgresInvalidCDCPositionBehaviorAdvanced.FAIL_SYNC));
        }

        return cdcBuilder;
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
            SourcePostgresVerifyCa.Builder sslBuilder = SourcePostgresVerifyCa.builder()
                    .caCertificate(validateRequiredField(verifyConfig.getCaCertificate(), "caCertificate"));

            sslBuilder.clientCertificate(Optional.ofNullable(verifyConfig.getClientCertificate()));
            sslBuilder.clientKey(Optional.ofNullable(verifyConfig.getClientKey()));
            sslBuilder.clientKeyPassword(Optional.ofNullable(verifyConfig.getClientKeyPassword()));
            postgresBuilder.sslMode(Optional.of(SourcePostgresSSLModes.of(sslBuilder.build())));
        } else {
            SourcePostgresVerifyFull.Builder sslBuilder = SourcePostgresVerifyFull.builder()
                    .caCertificate(validateRequiredField(verifyConfig.getCaCertificate(), "caCertificate"));

            sslBuilder.clientCertificate(Optional.ofNullable(verifyConfig.getClientCertificate()));
            sslBuilder.clientKey(Optional.ofNullable(verifyConfig.getClientKey()));
            sslBuilder.clientKeyPassword(Optional.ofNullable(verifyConfig.getClientKeyPassword()));

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

        if (tunnelMethod == null || tunnelMethod == SourceSSHTunnelMethod.NO_TUNNEL) {
            postgresBuilder.tunnelMethod(Optional.of(SourcePostgresSSHTunnelMethod.of(
                    SourcePostgresNoTunnel.builder().build()
            )));
            return;
        }

        switch (tunnelMethod) {
            case SSH_KEY_AUTH:
                SSHKeyAuthenticationDTO sshKeyConfig = (SSHKeyAuthenticationDTO) postgresConfig.getSshTunnelMethod();
                SourcePostgresSSHKeyAuthentication.Builder sshKeyBuilder = SourcePostgresSSHKeyAuthentication.builder()
                        .sshKey(validateRequiredField(sshKeyConfig.getSshKey(), "sshKey"))
                        .tunnelHost(validateRequiredField(sshKeyConfig.getTunnelHost(), "tunnelHost"))
                        .tunnelUser(validateRequiredField(sshKeyConfig.getTunnelUser(), "tunnelUser"))
                        .tunnelPort(Optional.ofNullable(sshKeyConfig.getTunnelPort()));
                postgresBuilder.tunnelMethod(Optional.of(SourcePostgresSSHTunnelMethod.of(sshKeyBuilder.build())));
                break;

            case SSH_PASSWORD_AUTH:
                PasswordAuthenticationDTO passwordAuthConfig = (PasswordAuthenticationDTO) postgresConfig.getSshTunnelMethod();
                SourcePostgresPasswordAuthentication.Builder passwordAuthBuilder = SourcePostgresPasswordAuthentication.builder()
                        .tunnelHost(validateRequiredField(passwordAuthConfig.getTunnelHost(), "tunnelHost"))
                        .tunnelUser(validateRequiredField(passwordAuthConfig.getTunnelUser(), "tunnelUser"))
                        .tunnelUserPassword(validateRequiredField(passwordAuthConfig.getTunnelUserPassword(), "tunnelUserPassword"))
                        .tunnelPort(Optional.ofNullable(passwordAuthConfig.getTunnelPort()));
                postgresBuilder.tunnelMethod(Optional.of(SourcePostgresSSHTunnelMethod.of(passwordAuthBuilder.build())));
                break;

            default:
                logger.warn("Unknown tunnel method: {}, using NO_TUNNEL as default", tunnelMethod);
                postgresBuilder.tunnelMethod(Optional.of(SourcePostgresSSHTunnelMethod.of(
                        SourcePostgresNoTunnel.builder().build()
                )));
                break;
        }
    }

    private void configureSchemas(SourcePostgres.Builder postgresBuilder, SourcePostgresDTO postgresConfig) {
        String schemasInput = postgresConfig.getSchemas();
        if (schemasInput == null || schemasInput.isBlank()) {
            postgresBuilder.schemas(Optional.empty());
            return;
        }
        List<String> schemaList = Arrays.stream(schemasInput.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        postgresBuilder.schemas(schemaList.isEmpty() ? Optional.empty() : Optional.of(schemaList));
    }


    private SourceConfiguration buildFileConfiguration(SourceFileDTO sourceFileConfiguration) {
        SourceFile.Builder fileBuilder = SourceFile.builder()
                .datasetName(validateRequiredField(
                        sourceFileConfiguration.getDatasetName(), "datasetName"))
                .url(validateRequiredField(
                        sourceFileConfiguration.getUrl(), "url"));

        // format: Optional<? extends FileFormat>
        Optional<FileFormat> fileFormatOptional = Optional
                .ofNullable(sourceFileConfiguration.getFormat())
                .map(this::buildFileFormat);
        fileBuilder.format(fileFormatOptional);

        fileBuilder.readerOptions(Optional.ofNullable(
                (sourceFileConfiguration.getReaderOptions())));

        StorageProvider storageProvider = buildStorageProvider(sourceFileConfiguration);
        fileBuilder.provider(storageProvider);

        return SourceConfiguration.of(fileBuilder.build());
    }

    private FileFormat buildFileFormat(FileFormatType formatType) {
        if (formatType == null) {
            return null;
        }
        return switch (formatType) {
            case CSV -> FileFormat.CSV;
            case JSON -> FileFormat.JSON;
            case JSONL -> FileFormat.JSONL;
            case EXCEL -> FileFormat.EXCEL;
            case EXCEL_BINARY -> FileFormat.EXCEL_BINARY;
            case FWF -> FileFormat.FWF;
            case FEATHER -> FileFormat.FEATHER;
            case PARQUET -> FileFormat.PARQUET;
            case YAML -> FileFormat.YAML;
        };
    }

    private StorageProvider buildStorageProvider(SourceFileDTO sourceFileConfiguration) {
        StorageProviderType providerType = sourceFileConfiguration.getProvider();
        if (providerType == null) {
            throw new IllegalArgumentException("provider cannot be null");
        }

        SourceFileStorageProviderDTO providerConfig = sourceFileConfiguration.getStorageProvider();
        if (providerConfig == null) {
            throw new IllegalArgumentException("storageProvider config cannot be null for provider " + providerType);
        }

        switch (providerType) {
            case HTTPS: {
                if (!(providerConfig instanceof HTTPSPublicWebDTO httpsConfig)) {
                    throw new IllegalArgumentException("storageProvider type mismatch: expected HTTPSPublicWebDTO");
                }
                HTTPSPublicWeb.Builder httpsBuilder = HTTPSPublicWeb.builder()
                        .userAgent(Optional.ofNullable(httpsConfig.getUserAgent()));
                return StorageProvider.of(httpsBuilder.build());
            }

            case S3: {
                if (!(providerConfig instanceof S3AmazonWebServicesDTO s3Config)) {
                    throw new IllegalArgumentException("storageProvider type mismatch: expected S3AmazonWebServicesDTO");
                }
                S3AmazonWebServices.Builder s3Builder = S3AmazonWebServices.builder()
                        .awsAccessKeyId(Optional.ofNullable(s3Config.getAwsAccessKeyId()))
                        .awsSecretAccessKey(Optional.ofNullable(s3Config.getAwsSecretAccessKey()));
                return StorageProvider.of(s3Builder.build());
            }

            case GCS: {
                if (!(providerConfig instanceof GCSGoogleCloudStorageDTO gcsConfig)) {
                    throw new IllegalArgumentException("storageProvider type mismatch: expected GCSGoogleCloudStorageDTO");
                }
                GCSGoogleCloudStorage.Builder gcsBuilder = GCSGoogleCloudStorage.builder()
                        .serviceAccountJson(Optional.ofNullable(gcsConfig.getServiceAccountJson()));
                return StorageProvider.of(gcsBuilder.build());
            }

            case AZ_BLOB: {
                if (!(providerConfig instanceof AzBlobAzureBlobStorageDTO azConfig)) {
                    throw new IllegalArgumentException("storageProvider type mismatch: expected AzBlobAzureBlobStorageDTO");
                }
                String storageAccount = validateRequiredField(azConfig.getStorageAccount(), "storageAccount");
                AzBlobAzureBlobStorage.Builder azBlobBuilder = AzBlobAzureBlobStorage.builder()
                        .storageAccount(storageAccount)
                        .sasToken(Optional.ofNullable(azConfig.getSasToken()))
                        .sharedKey(Optional.ofNullable(azConfig.getSharedKey()));
                return StorageProvider.of(azBlobBuilder.build());
            }

            case SSH: {
                if (!(providerConfig instanceof SSH_SCP_SFTP_ProtocolDTO sshConfig)) {
                    throw new IllegalArgumentException("storageProvider type mismatch: expected SSH_SCP_SFTP_ProtocolDTO");
                }
                String host = validateRequiredField(sshConfig.getHost(), "host");
                String user = validateRequiredField(sshConfig.getUser(), "user");
                SSHSecureShell.Builder sshBuilder = SSHSecureShell.builder()
                        .host(host)
                        .user(user)
                        .password(Optional.ofNullable(sshConfig.getPassword()))
                        .port(Optional.ofNullable(sshConfig.getPort()));
                return StorageProvider.of(sshBuilder.build());
            }

            case SCP: {
                if (!(providerConfig instanceof SSH_SCP_SFTP_ProtocolDTO scpConfig)) {
                    throw new IllegalArgumentException("storageProvider type mismatch: expected SSH_SCP_SFTP_ProtocolDTO");
                }
                String host = validateRequiredField(scpConfig.getHost(), "host");
                String user = validateRequiredField(scpConfig.getUser(), "user");
                SCPSecureCopyProtocol.Builder scpBuilder = SCPSecureCopyProtocol.builder()
                        .host(host)
                        .user(user)
                        .password(Optional.ofNullable(scpConfig.getPassword()))
                        .port(Optional.ofNullable(scpConfig.getPort()));
                return StorageProvider.of(scpBuilder.build());
            }

            case SFTP: {
                if (!(providerConfig instanceof SSH_SCP_SFTP_ProtocolDTO sftpConfig)) {
                    throw new IllegalArgumentException("storageProvider type mismatch: expected SSH_SCP_SFTP_ProtocolDTO");
                }
                String host = validateRequiredField(sftpConfig.getHost(), "host");
                String user = validateRequiredField(sftpConfig.getUser(), "user");
                SFTPSecureFileTransferProtocol.Builder sftpBuilder = SFTPSecureFileTransferProtocol.builder()
                        .host(host)
                        .user(user)
                        .password(Optional.ofNullable(sftpConfig.getPassword()))
                        .port(Optional.ofNullable(sftpConfig.getPort()));
                return StorageProvider.of(sftpBuilder.build());
            }

            case LOCAL: {
                LocalFilesystemLimited.Builder localBuilder = LocalFilesystemLimited.builder();
                return StorageProvider.of(localBuilder.build());
            }

            default:
                throw new IllegalArgumentException("Unsupported storage provider: " + providerType);
        }
    }


    private String validateRequiredField(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
        return value;
    }
}
