package com.company.airbyte.view.source.fragment;

import com.company.airbyte.dto.source.SourceDatabaseDTO;
import com.company.airbyte.dto.source.common.SSHKeyAuthenticationDTO;
import com.company.airbyte.dto.source.common.SourceSSHTunnelMethod;
import com.company.airbyte.dto.source.common.SourceSSHTunnelMethodDTO;
import com.company.airbyte.dto.source.mssql.SourceMssqlDTO;
import com.company.airbyte.dto.source.postgres.SourcePostgresDTO;
import com.company.airbyte.dto.source.postgres.SourcePostgresSSLModes;
import com.company.airbyte.dto.source.postgres.SourcePostgresUpdateMethod;
import com.company.airbyte.dto.source.postgres.SourcePostgresVerifyDTO;
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
    private InstanceContainer<SourceDatabaseDTO> sourceDatabaseDc;
    @ViewComponent
    private JmixFormLayout postgresVerifyForm;

    @Autowired
    private Metadata metadata;
    @ViewComponent
    private InstanceContainer<SourcePostgresVerifyDTO> postgresVerifyDc;
    @ViewComponent
    private InstanceContainer<SourcePostgresDTO> postgresDc;
    @ViewComponent
    private InstanceContainer<SourceMssqlDTO> mssqlDc;
    @ViewComponent
    private JmixFormLayout postgresCdcForm;

    @Subscribe(target = Target.HOST_CONTROLLER)
    public void onHostInit(final View.InitEvent event) {

    }

    @Subscribe(target = Target.HOST_CONTROLLER)
    public void onHostReady(final View.ReadyEvent event) {
        SourceDatabaseDTO sourceDb = sourceDatabaseDc.getItemOrNull();
        if (sourceDb != null) {
            visibleFieldsByDbType(sourceDb.getDatabaseType());
            visibleFieldsBySshTunnelMethod(sourceDb.getTunnelMethod());
        }
    }

    @Subscribe(id = "sourceDatabaseDc", target = Target.DATA_CONTAINER)
    public void onSourceDcItemPropertyChange(final InstanceContainer.ItemPropertyChangeEvent<SourceDatabaseDTO> event) {
        if ("databaseType".equals(event.getProperty()) && event.getValue() != null) {
            DatabaseType dbType = DatabaseType.fromId(event.getValue().toString());
            visibleFieldsByDbType(dbType);
        }

        if ("tunnelMethod".equals(event.getProperty()) && event.getValue() != null) {
            SourceSSHTunnelMethod tunnelMethod = SourceSSHTunnelMethod.fromId(event.getValue().toString());
            visibleFieldsBySshTunnelMethod(tunnelMethod);
        }

    }

    @Subscribe(id = "postgresDc", target = Target.DATA_CONTAINER)
    public void onPostgresDcItemPropertyChange(final InstanceContainer.ItemPropertyChangeEvent<SourcePostgresDTO> event) {
        if ("sslMode".equals(event.getProperty()) && event.getValue() != null) {
            SourcePostgresSSLModes sslMode = SourcePostgresSSLModes.fromId(event.getValue().toString());
            updatePostgresSslCertificateForm(sslMode);
        }

        if ("replicationMethod".equals(event.getProperty()) && event.getValue() != null) {
            SourcePostgresUpdateMethod replicationMethod = SourcePostgresUpdateMethod.fromId(event.getValue().toString());
            updatePostgresCdcForm(replicationMethod);
        }

    }

    private void updatePostgresSslCertificateForm(SourcePostgresSSLModes sslMode) {
        postgresVerifyForm.setVisible(false);
        ensureVerifyItemsIfNeeded(sslMode);
        if (sslMode != null) {
            switch (sslMode) {
                case VERIFY_CA:
                    postgresVerifyForm.setVisible(true);
                    break;
                case VERIFY_FULL:
                    postgresVerifyForm.setVisible(true);
                    break;
                default:
                    // Các SSL mode khác không cần certificate form
                    break;
            }
        }
    }

    private void updatePostgresCdcForm(SourcePostgresUpdateMethod replicationMethod) {
        postgresCdcForm.setVisible(false);
//        ensureCdcItemsIfNeeded(replicationMethod);
        if (replicationMethod.equals(SourcePostgresUpdateMethod.CDC)) {
            postgresCdcForm.setVisible(true);
        }
    }


    public void visibleFieldsByDbType(DatabaseType dbType) {
        hideAllForms();
        clearChildContainers();
        resetSourceExceptDbType();

        if (dbType != null) {
            switch (dbType) {
                case POSTGRES:
                    postgresForm.setVisible(true);
                    if (postgresDc.getItemOrNull() == null) {
                        postgresDc.setItem(metadata.create(SourcePostgresDTO.class));
                    }
                    break;
                case MSSQL:
                    mssqlForm.setVisible(true);
                    if (mssqlDc.getItemOrNull() == null) {
                        mssqlDc.setItem(metadata.create(SourceMssqlDTO.class));
                    }
                    break;
                case MYSQL:
                    //
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
                    passwordAuthForm.setVisible(true);
//                    createPasswordAuthInstance();
                    break;
                case SSH_KEY_AUTH:
                    sshKeyAuthForm.setVisible(true);
//                    createSSHKeyAuthInstance();
                    break;
                case NO_TUNNEL:
                    // Không hiển thị form nào
                    break;
            }
        }
    }

    private void ensureDbSpecificItems(DatabaseType type) {
        // Postgres
        if (type == DatabaseType.POSTGRES) {
            if (postgresDc.getItemOrNull() == null) {
                postgresDc.setItem(metadata.create(SourcePostgresDTO.class));
            }
        } else {
            postgresDc.setItem(null);
        }
        // MSSQL (nếu bạn dùng)
        if (type == DatabaseType.MSSQL) {
            if (mssqlDc.getItemOrNull() == null) {
                mssqlDc.setItem(metadata.create(SourceMssqlDTO.class));
            }
        } else {
            mssqlDc.setItem(null);
        }
        // Nếu là MySQL… thêm tương tự khi có form riêng
    }

    private void ensureVerifyItemsIfNeeded(SourcePostgresSSLModes mode) {
        boolean need = mode == SourcePostgresSSLModes.VERIFY_CA || mode == SourcePostgresSSLModes.VERIFY_FULL;
        if (need) {
            if (postgresVerifyDc.getItemOrNull() == null) {
                postgresVerifyDc.setItem(metadata.create(SourcePostgresVerifyDTO.class));
            }
        } else {
            postgresVerifyDc.setItem(null);
        }
    }

    private void resetSourceExceptDbType() {
        SourceDatabaseDTO sourceDatabaseDcNew = sourceDatabaseDc.getItemOrNull();
        if (sourceDatabaseDcNew != null) {
            DatabaseType type = sourceDatabaseDcNew.getDatabaseType();
            sourceDatabaseDcNew = metadata.create(SourceDatabaseDTO.class);
            sourceDatabaseDcNew.setDatabaseType(type);
            sourceDatabaseDc.setItem(sourceDatabaseDcNew);
        }
    }

    private void clearChildContainers() {
        postgresDc.setItem(null);
        mssqlDc.setItem(null);
        postgresVerifyDc.setItem(null);
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