package com.company.airbyte.view.source.fragment;

import com.company.airbyte.dto.source.SourceDatabaseDTO;
import com.company.airbyte.dto.source.common.PasswordAuthenticationDTO;
import com.company.airbyte.dto.source.common.SSHKeyAuthenticationDTO;
import com.company.airbyte.dto.source.common.SourceSSHTunnelMethod;
import com.company.airbyte.dto.source.common.SourceSSHTunnelMethodDTO;
import com.company.airbyte.dto.source.mssql.SourceMssqlDTO;
import com.company.airbyte.dto.source.postgres.*;
import com.company.airbyte.entity.DatabaseType;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.jmix.core.Metadata;
import io.jmix.flowui.component.formlayout.JmixFormLayout;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.fragmentrenderer.FragmentRenderer;
import io.jmix.flowui.fragmentrenderer.RendererItemContainer;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.Target;
import io.jmix.flowui.view.View;
import io.jmix.flowui.view.ViewComponent;
import org.springframework.beans.factory.annotation.Autowired;

@FragmentDescriptor("source-database-fragment.xml")
@RendererItemContainer("sourceDatabaseDc")
public class SourceDatabaseFragment extends FragmentRenderer<VerticalLayout, SourceDatabaseDTO> {

    @ViewComponent
    private JmixFormLayout postgresForm;

    @ViewComponent
    private JmixFormLayout mssqlForm;

    @ViewComponent
    private JmixFormLayout passwordAuthForm;

    @ViewComponent
    private JmixFormLayout sshKeyAuthForm;

    @ViewComponent
    private JmixFormLayout postgresVerifyForm;

    @ViewComponent
    private JmixFormLayout postgresCdcForm;

    @Autowired
    private Metadata metadata;

    @ViewComponent
    private InstanceContainer<SourceDatabaseDTO> sourceDatabaseDc;

    @ViewComponent
    private InstanceContainer<SourcePostgresVerifyDTO> postgresVerifyDc;
    @ViewComponent
    private InstanceContainer<SourcePostgresDTO> postgresDc;
    @ViewComponent
    private InstanceContainer<ReadChangesUsingWriteAheadLogCDCDTO> postgresCdcDc;
    @ViewComponent
    private InstanceContainer<SourceMssqlDTO> mssqlDc;
    @ViewComponent
    private InstanceContainer<SSHKeyAuthenticationDTO> sshKeyAuthDc;
    @ViewComponent
    private InstanceContainer<PasswordAuthenticationDTO> passwordAuthDc;

    @Override
    public void setItem(SourceDatabaseDTO item) {
        super.setItem(item);
        sourceDatabaseDc.setItem(item);
        initializeChildContainers(item);

        // Khi load dữ liệu từ DB để edit, KHÔNG reset item
        visibleFieldsByDbType(item.getDatabaseType(), false);
        visibleFieldsBySshTunnelMethod(item.getTunnelMethod());

        if (item instanceof SourcePostgresDTO pg) {
            if (pg.getSslMode() != null) {
                updatePostgresSslCertificateForm(pg.getSslMode());
            }
            if (pg.getReplicationMethod() != null) {
                updatePostgresCdcForm(pg.getReplicationMethod());
            }
        }
    }

    public SourceDatabaseDTO getItem() {
        return sourceDatabaseDc.getItemOrNull();
    }


    private void initializeChildContainers(SourceDatabaseDTO item) {
        if (item == null) return;

        DatabaseType dbType = item.getDatabaseType();
        if (dbType != null) {
            switch (dbType) {
                case POSTGRES:

                    SourcePostgresDTO postgresDTO = extractOrCreatePostgresDTO(item);
                    postgresDc.setItem(postgresDTO);
                    // chỉ set lại root khi thực sự reset chuyển loại DB
                    if (sourceDatabaseDc.getItemOrNull() != postgresDTO) {
                        sourceDatabaseDc.setItem(postgresDTO);
                    }
                    // Khởi tạo verify form nếu cần
                    if (postgresDTO.getSslMode() != null &&
                            (postgresDTO.getSslMode().equals(SourcePostgresSSLModes.VERIFY_CA) ||
                                    postgresDTO.getSslMode().equals(SourcePostgresSSLModes.VERIFY_FULL))) {
                        SourcePostgresVerifyDTO verifyDTO = postgresDTO.getVerifyFullDTO();
                        if (verifyDTO == null) {
                            verifyDTO = metadata.create(SourcePostgresVerifyDTO.class);
                            postgresDTO.setVerifyFullDTO(verifyDTO);
                        }
                        postgresVerifyDc.setItem(verifyDTO);
                    }

                    // Khởi tạo CDC form nếu cần
                    if (postgresDTO.getReplicationMethod() != null &&
                            postgresDTO.getReplicationMethod().equals(SourcePostgresUpdateMethod.CDC)) {
                        ReadChangesUsingWriteAheadLogCDCDTO cdcDTO = postgresDTO.getCdcDTO();
                        if (cdcDTO == null) {
                            cdcDTO = metadata.create(ReadChangesUsingWriteAheadLogCDCDTO.class);
                            postgresDTO.setCdcDTO(cdcDTO);
                        }
                        postgresCdcDc.setItem(cdcDTO);
                    }
                    break;

                case MSSQL:
                    SourceMssqlDTO mssqlDTO = extractOrCreateMssqlDTO(item);
                    mssqlDc.setItem(mssqlDTO);
                    if (sourceDatabaseDc.getItemOrNull() != mssqlDTO) {
                        sourceDatabaseDc.setItem(mssqlDTO);
                    }
                    break;

                case MYSQL:
                    // MySQL specific logic if needed
                    break;
            }
        }

        initializeSSHContainers(item);
    }

    private void initializeSSHContainers(SourceDatabaseDTO item) {
        applySshMethodToRoot(item.getTunnelMethod(), item);
    }

    private void applySshMethodToRoot(SourceSSHTunnelMethod tunnelMethod, SourceDatabaseDTO root) {
        if (tunnelMethod == null || root == null) return;

        switch (tunnelMethod) {
            case SSH_PASSWORD_AUTH: {
                PasswordAuthenticationDTO passwordAuth = extractOrCreatePasswordAuth(root);
                passwordAuthDc.setItem(passwordAuth);
                root.setSshTunnelMethod(passwordAuth);
                break;
            }
            case SSH_KEY_AUTH: {
                SSHKeyAuthenticationDTO sshKeyAuth = extractOrCreateSSHKeyAuth(root);
                sshKeyAuthDc.setItem(sshKeyAuth);
                root.setSshTunnelMethod(sshKeyAuth);
                break;
            }
            case NO_TUNNEL:
            default: {
                root.setSshTunnelMethod(null);
                break;
            }
        }
    }

    private SourcePostgresDTO extractOrCreatePostgresDTO(SourceDatabaseDTO item) {
        // Nếu item đã là SourcePostgresDTO thì return luôn
        if (item instanceof SourcePostgresDTO) {
            return (SourcePostgresDTO) item;
        }

        return metadata.create(SourcePostgresDTO.class);
    }

    private SourceMssqlDTO extractOrCreateMssqlDTO(SourceDatabaseDTO item) {
        if (item instanceof SourceMssqlDTO) {
            return (SourceMssqlDTO) item;
        }

        return metadata.create(SourceMssqlDTO.class);
    }

    private PasswordAuthenticationDTO extractOrCreatePasswordAuth(SourceDatabaseDTO item) {
        SourceSSHTunnelMethodDTO sshMethod = item.getSshTunnelMethod();
        if (sshMethod instanceof PasswordAuthenticationDTO) {
            return (PasswordAuthenticationDTO) sshMethod;
        }

        return metadata.create(PasswordAuthenticationDTO.class);
    }

    private SSHKeyAuthenticationDTO extractOrCreateSSHKeyAuth(SourceDatabaseDTO item) {
        SourceSSHTunnelMethodDTO sshMethod = item.getSshTunnelMethod();
        if (sshMethod instanceof SSHKeyAuthenticationDTO) {
            return (SSHKeyAuthenticationDTO) sshMethod;
        }

        return metadata.create(SSHKeyAuthenticationDTO.class);
    }

    @Subscribe(id = "sourceDatabaseDc", target = Target.DATA_CONTAINER)
    public void onSourceDcItemPropertyChange(final InstanceContainer.ItemPropertyChangeEvent<SourceDatabaseDTO> event) {
        if ("databaseType".equals(event.getProperty()) && event.getValue() != null) {
            DatabaseType dbType = DatabaseType.fromId(event.getValue().toString());
            // Người dùng đổi loại DB trên UI -> cần reset để đổi DTO con
            visibleFieldsByDbType(dbType, true);
        }

        if ("tunnelMethod".equals(event.getProperty()) && event.getValue() != null) {
            SourceSSHTunnelMethod tunnelMethod = SourceSSHTunnelMethod.fromId(event.getValue().toString());
            // Khởi tạo/gắn DTO SSH ngay khi người dùng chọn method
            SourceDatabaseDTO root = sourceDatabaseDc.getItemOrNull();
            applySshMethodToRoot(tunnelMethod, root);
            visibleFieldsBySshTunnelMethod(tunnelMethod);
        }

    }

    @Subscribe(id = "postgresDc", target = Target.DATA_CONTAINER)
    public void onPostgresDcItemPropertyChange(final InstanceContainer.ItemPropertyChangeEvent<SourcePostgresDTO> event) {
        if ("sslMode".equals(event.getProperty()) && event.getValue() != null) {
            SourcePostgresSSLModes sslMode = SourcePostgresSSLModes.fromId(event.getValue().toString());
            updatePostgresSslCertificateForm(sslMode);
            // Khởi tạo/ghép verify DTO khi người dùng bật verify-ca/full
            SourcePostgresDTO pg = postgresDc.getItemOrNull();
            if (pg != null) {
                if (sslMode.equals(SourcePostgresSSLModes.VERIFY_CA) || sslMode.equals(SourcePostgresSSLModes.VERIFY_FULL)) {
                    SourcePostgresVerifyDTO verifyDTO = pg.getVerifyFullDTO();
                    if (verifyDTO == null) {
                        verifyDTO = metadata.create(SourcePostgresVerifyDTO.class);
                        pg.setVerifyFullDTO(verifyDTO);
                    }
                    postgresVerifyDc.setItem(verifyDTO);
                } else {
                    // chế độ không yêu cầu chứng chỉ -> bỏ tham chiếu để JSON gọn gàng
                    pg.setVerifyFullDTO(null);
                    postgresVerifyDc.setItem(metadata.create(SourcePostgresVerifyDTO.class));
                }
            }
        }

        if ("replicationMethod".equals(event.getProperty()) && event.getValue() != null) {
            SourcePostgresUpdateMethod replicationMethod = SourcePostgresUpdateMethod.fromId(event.getValue().toString());
            updatePostgresCdcForm(replicationMethod);
            // Khởi tạo/ghép CDC DTO khi người dùng chọn CDC
            SourcePostgresDTO pg = postgresDc.getItemOrNull();
            if (pg != null) {
                if (replicationMethod.equals(SourcePostgresUpdateMethod.CDC)) {
                    ReadChangesUsingWriteAheadLogCDCDTO cdcDTO = pg.getCdcDTO();
                    if (cdcDTO == null) {
                        cdcDTO = metadata.create(ReadChangesUsingWriteAheadLogCDCDTO.class);
                        pg.setCdcDTO(cdcDTO);
                    }
                    postgresCdcDc.setItem(cdcDTO);
                } else {
                    pg.setCdcDTO(null);
                    postgresCdcDc.setItem(metadata.create(ReadChangesUsingWriteAheadLogCDCDTO.class));
                }
            }
        }

    }

    private void updatePostgresSslCertificateForm(SourcePostgresSSLModes sslMode) {
        postgresVerifyForm.setVisible(false);

        if (sslMode.equals(SourcePostgresSSLModes.VERIFY_CA) || sslMode.equals(SourcePostgresSSLModes.VERIFY_FULL)) {
            postgresVerifyForm.setVisible(true);
        }
    }

    private void updatePostgresCdcForm(SourcePostgresUpdateMethod replicationMethod) {
        postgresCdcForm.setVisible(false);
        if (replicationMethod.equals(SourcePostgresUpdateMethod.CDC)) {
            postgresCdcForm.setVisible(true);
        }
    }

    public void visibleFieldsByDbType(DatabaseType dbType, boolean shouldReset) {
        hideAllForms();
        // Chỉ reset khi người dùng đổi loại DB; khi mở bản ghi edit thì giữ nguyên
        if (shouldReset) {
            resetSourceExceptDbType();
        }

        if (dbType != null) {
            // đảm bảo các container con có item phù hợp trước khi show form
            initializeChildContainers(sourceDatabaseDc.getItemOrNull());

            switch (dbType) {
                case POSTGRES:
                    // đảm bảo có SourcePostgresDTO trong postgresDc
                    if (postgresDc.getItemOrNull() == null) {
                        SourcePostgresDTO pg = extractOrCreatePostgresDTO(sourceDatabaseDc.getItem());
                        postgresDc.setItem(pg);

                    }
                    postgresForm.setVisible(true);
                    break;
                case MSSQL:
                    if (mssqlDc.getItemOrNull() == null) {
                        SourceMssqlDTO mssql = extractOrCreateMssqlDTO(sourceDatabaseDc.getItem());
                        mssqlDc.setItem(mssql);
                    }
                    mssqlForm.setVisible(true);
                    break;
                case MYSQL:
                    // TODO: thêm form riêng nếu cần
                    break;
            }
        }
    }


    private void visibleFieldsBySshTunnelMethod(SourceSSHTunnelMethod tunnelMethod) {
        // Hide all SSH forms
        passwordAuthForm.setVisible(false);
        sshKeyAuthForm.setVisible(false);

        if (tunnelMethod != null) {
            switch (tunnelMethod) {
                case SSH_PASSWORD_AUTH:
                    // đảm bảo container có item để binding 2 chiều
                    if (passwordAuthDc.getItemOrNull() == null) {
                        PasswordAuthenticationDTO pwd = metadata.create(PasswordAuthenticationDTO.class);
                        passwordAuthDc.setItem(pwd);
                        SourceDatabaseDTO root = sourceDatabaseDc.getItemOrNull();
                        if (root != null) root.setSshTunnelMethod(pwd);
                    }
                    passwordAuthForm.setVisible(true);
                    break;
                case SSH_KEY_AUTH:
                    if (sshKeyAuthDc.getItemOrNull() == null) {
                        SSHKeyAuthenticationDTO key = metadata.create(SSHKeyAuthenticationDTO.class);
                        sshKeyAuthDc.setItem(key);
                        SourceDatabaseDTO root = sourceDatabaseDc.getItemOrNull();
                        if (root != null) root.setSshTunnelMethod(key);
                    }
                    sshKeyAuthForm.setVisible(true);
                    break;
                case NO_TUNNEL:
                    // Không hiển thị form nào
                    break;
            }
        }
    }

    private void resetSourceExceptDbType() {
        SourceDatabaseDTO cur = sourceDatabaseDc.getItemOrNull();
        if (cur != null) {
            DatabaseType type = cur.getDatabaseType();
            SourceDatabaseDTO fresh;
            if (type != null) {
                switch (type) {
                    case POSTGRES:
                        fresh = metadata.create(SourcePostgresDTO.class);
                        break;
                    case MSSQL:
                        fresh = metadata.create(SourceMssqlDTO.class);
                        break;
                    case MYSQL:
                    default:
                        fresh = metadata.create(SourceDatabaseDTO.class);
                        break;
                }
            } else {
                fresh = metadata.create(SourceDatabaseDTO.class);
            }
            fresh.setDatabaseType(type);
            fresh.setTunnelMethod(SourceSSHTunnelMethod.NO_TUNNEL); // default
            sourceDatabaseDc.setItem(fresh);
        }
    }


    private void hideAllForms() {
        postgresForm.setVisible(false);
        mssqlForm.setVisible(false);
        postgresCdcForm.setVisible(false);
        postgresVerifyForm.setVisible(false);
        passwordAuthForm.setVisible(false);
        sshKeyAuthForm.setVisible(false);
    }

}