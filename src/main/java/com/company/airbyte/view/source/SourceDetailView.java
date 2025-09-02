package com.company.airbyte.view.source;

import com.company.airbyte.dto.source.SourceDTO;
import com.company.airbyte.dto.source.SourceDatabaseDTO;
import com.company.airbyte.dto.source.postgres.SourcePostgresDTO;
import com.company.airbyte.dto.source.common.SourceSSHTunnelMethod;
import com.company.airbyte.entity.DatabaseType;
import com.company.airbyte.entity.Source;
import com.company.airbyte.entity.SourceType;
import com.company.airbyte.view.main.MainView;
import com.company.airbyte.view.source.fragment.SourceDatabaseFragment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.core.Metadata;
import io.jmix.flowui.Fragments;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

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

    private SourceDatabaseFragment sourceDatabaseFragment;

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
                    pg.setDatabaseType(DatabaseType.POSTGRES);
                    pg.setTunnelMethod(SourceSSHTunnelMethod.NO_TUNNEL);
                    source.setConfiguration(pg);
                    break;
                }
                case FILE: {
                    // TODO: Implement file source details
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
                    DataContext dataContext = getViewData().getDataContext();
                    dataContext.setModified(source, true);
                    break;
                }
                case FILE: {

                }
            }
        }
    }
    
}
