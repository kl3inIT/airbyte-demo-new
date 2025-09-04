package com.company.airbyte.view.source;

import com.airbyte.api.models.shared.SourcePostgresSSLModes;
import com.airbyte.api.models.shared.SourceResponse;
import com.company.airbyte.dto.source.SourceDTO;
import com.company.airbyte.dto.source.SourceDatabaseDTO;
import com.company.airbyte.dto.source.file.FileFormatType;
import com.company.airbyte.dto.source.file.SourceFileDTO;
import com.company.airbyte.dto.source.postgres.SourcePostgresDTO;
import com.company.airbyte.dto.source.common.SourceSSHTunnelMethod;
import com.company.airbyte.dto.source.postgres.SourcePostgresSSLModesType;
import com.company.airbyte.dto.source.postgres.SourcePostgresUpdateMethodType;
import com.company.airbyte.entity.DataFormat;
import com.company.airbyte.entity.DatabaseType;
import com.company.airbyte.entity.Source;
import com.company.airbyte.entity.SourceType;
import com.company.airbyte.service.AirbyteService;
import com.company.airbyte.view.main.MainView;
import com.company.airbyte.view.source.fragment.SourceDatabaseFragment;
import com.company.airbyte.view.source.fragment.SourceFileFragment;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.router.Route;
import io.jmix.core.Metadata;
import io.jmix.flowui.Fragments;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Route(value = "sources/:id", layout = MainView.class)
@ViewController(id = "Source.detail")
@ViewDescriptor(path = "source-detail-view.xml")
@EditedEntityContainer("sourceDc")
public class SourceDetailView extends StandardDetailView<Source> {

    @Autowired
    private Metadata metadata;

    private SourceType requestedType;

    @ViewComponent
    private VerticalLayout sourceDetailVbox;

    @Autowired
    private Fragments fragments;

    @Autowired
    private AirbyteService airbyteService;

    private SourceDatabaseFragment sourceDatabaseFragment;
    @Autowired
    private Notifications notifications;

    private SourceFileFragment sourceFileFragment;
    @Autowired
    private UiComponents uiComponents;

    @Subscribe
    public void onQueryParametersChange(QueryParametersChangeEvent event) {
        requestedType = event.getQueryParameters()
                .getSingleParameter("type")
                .map(this::safeParseSourceType)
                .orElse(null);
    }

    private SourceType safeParseSourceType(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return SourceType.fromId(raw);
        } catch (Exception ignored) {
            return null;
        }
    }

    @Subscribe
    public void onInitEntity(final InitEntityEvent<Source> event) {
        Source source = event.getEntity();

        if (requestedType != null) {
            source.setSourceType(requestedType);

            switch (requestedType) {
                case DATABASE: {
                    SourcePostgresDTO pg = metadata.create(SourcePostgresDTO.class);
                    source.setName("Postgres");
                    pg.setDatabaseType(DatabaseType.POSTGRES);
                    pg.setTunnelMethod(SourceSSHTunnelMethod.NO_TUNNEL);
                    pg.setSslMode(SourcePostgresSSLModesType.DISABLE);
                    pg.setReplicationMethod(SourcePostgresUpdateMethodType.CDC);
                    pg.setSchemas("public");
                    pg.setPort(5432L);
                    source.setConfiguration(pg);
                    break;
                }
                case FILE: {
                    SourceFileDTO file = metadata.create(SourceFileDTO.class);
                    source.setName("File (CSV, JSON, Excel, Feather, Parquet)");
                    file.setFormat(FileFormatType.CSV);
                    source.setConfiguration(file);
                    break;
                }
                case API: {
                    // TODO: Implement API source details
                    break;
                }
                default: {
                    // TODO: Handle unknown source type
                }
            }
        }
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        sourceDetailVbox.removeAll();

        SourceType sourceType = getEditedEntity().getSourceType();
        if (sourceType != null) {
            switch (sourceType) {
                case DATABASE: {
                    sourceDatabaseFragment = fragments.create(this, SourceDatabaseFragment.class);
                    SourceDatabaseDTO sourceDatabaseDTO = (SourceDatabaseDTO) getEditedEntity().getConfiguration();
                    sourceDatabaseFragment.setItem(sourceDatabaseDTO);
                    sourceDetailVbox.add(sourceDatabaseFragment);
                    break;
                }
                case FILE: {
                    sourceFileFragment = fragments.create(this, SourceFileFragment.class);

                    SourceDTO cfg = getEditedEntity().getConfiguration();
                    SourceFileDTO sourceFileDTO;
                    if (cfg instanceof SourceFileDTO) {
                        sourceFileDTO = (SourceFileDTO) cfg;
                    } else {
                        sourceFileDTO = metadata.create(SourceFileDTO.class);
                        getEditedEntity().setConfiguration(sourceFileDTO);
                    }

                    sourceFileFragment.setItem(sourceFileDTO);
                    sourceDetailVbox.add(sourceFileFragment);
                    break;
                }
            }
        }
    }

    @Subscribe
    public void onBeforeSave(final BeforeSaveEvent event) {
        Source source = getEditedEntity();
        SourceType sourceType = getEditedEntity().getSourceType();
        if (sourceType != null) {
            switch (sourceType) {
                case DATABASE: {
                    SourceDatabaseDTO sourceDatabaseDto = sourceDatabaseFragment.getItem();
                    source.setConfiguration(sourceDatabaseDto);

                    try {
                        SourceResponse resp = airbyteService.upsertSourceOnAirbyte(source);

                        if (resp.sourceId() != null) {
                            source.setSourceID(UUID.fromString(resp.sourceId()));
                        }
                        if (resp.workspaceId() != null) {
                            source.setWorkspaceId(UUID.fromString(resp.workspaceId()));
                        }

                        source.setDataFormat(DataFormat.QUERY);

                        getViewData().getDataContext().setModified(source, true);

                    } catch (Exception ex) {
                        event.preventSave();
                        notifications.create("Airbyte create destination failed")
                                .withType(Notifications.Type.ERROR).show();
                    }
                    break;
                }
                case FILE:
                    if (sourceFileFragment != null) {
                        SourceFileDTO fileDto = sourceFileFragment.getItem();
                        source.setConfiguration(fileDto);
                    }

                    try {
                        SourceResponse resp = airbyteService.upsertSourceOnAirbyte(source);

                        if (resp.sourceId() != null) {
                            source.setSourceID(UUID.fromString(resp.sourceId()));
                        }
                        if (resp.workspaceId() != null) {
                            source.setWorkspaceId(UUID.fromString(resp.workspaceId()));
                        }

                        // File -> lưu dạng bảng
                        source.setDataFormat(DataFormat.TABLE);
                        getViewData().getDataContext().setModified(source, true);

                    } catch (Exception ex) {
                        event.preventSave();
                        notifications.create("Airbyte create source failed")
                                .withType(Notifications.Type.ERROR).show();
                    }
                    break;
            }
        }
    }


}
