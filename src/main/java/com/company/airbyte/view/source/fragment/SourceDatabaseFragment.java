package com.company.airbyte.view.source.fragment;

import com.company.airbyte.dto.source.SourceDatabaseDTO;
import com.company.airbyte.dto.source.common.SourceSSHTunnelMethod;
import com.company.airbyte.dto.source.common.SourceSSHTunnelMethodDTO;
import com.company.airbyte.entity.DatabaseType;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.fragmentrenderer.FragmentRenderer;
import io.jmix.flowui.fragmentrenderer.RendererItemContainer;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.Target;
import io.jmix.flowui.view.View;
import io.jmix.flowui.view.ViewComponent;

@FragmentDescriptor("source-database-fragment.xml")
@RendererItemContainer("sourceDc")
public class SourceDatabaseFragment extends FragmentRenderer<VerticalLayout, SourceDatabaseDTO> {

    @ViewComponent
    private FormLayout postgresForm;

    @ViewComponent
    private FormLayout mssqlForm;

    @ViewComponent
    private FormLayout passwordAuthForm;

    @ViewComponent
    private FormLayout sshKeyAuthForm;

    @Subscribe(target = Target.HOST_CONTROLLER)
    public void onHostInit(final View.InitEvent event) {

    }

    @Subscribe(id = "sourceDc", target = Target.DATA_CONTAINER)
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

    public void visibleFieldsByDbType(DatabaseType dbType) {
        postgresForm.setVisible(false);
        mssqlForm.setVisible(false);

        if (dbType != null) {
            switch (dbType) {
                case POSTGRES:
                    postgresForm.setVisible(true);
                    break;
                case MSSQL:
                    mssqlForm.setVisible(true);
                    break;
                case MYSQL:
                    // MySQL có thể không cần các field đặc biệt hoặc thêm form riêng
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


}