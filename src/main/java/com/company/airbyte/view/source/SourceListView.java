package com.company.airbyte.view.source;

import com.company.airbyte.entity.Source;
import com.company.airbyte.entity.SourceType;
import com.company.airbyte.view.main.MainView;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;


@Route(value = "sources", layout = MainView.class)
@ViewController(id = "Source.list")
@ViewDescriptor(path = "source-list-view.xml")
@LookupComponent("sourcesDataGrid")
@DialogMode(width = "64em")
public class SourceListView extends StandardListView<Source> {

    @Autowired
    private ViewNavigators viewNavigators;

    @Subscribe("sourcesDataGrid.createDb")
    public void onSourcesDataGridCreateDb(final ActionPerformedEvent event) {
        String sourceType = SourceType.DATABASE.getId();
        viewNavigators.detailView(this, Source.class)
                .withQueryParameters(QueryParameters.of("type", sourceType))
                .newEntity()
                .navigate();

    }

    @Subscribe("sourcesDataGrid.createFile")
    public void onSourcesDataGridCreateFile(final ActionPerformedEvent event) {
        String sourceType = SourceType.FILE.getId();
        viewNavigators.detailView(this, Source.class)
                .withQueryParameters(QueryParameters.of("type", sourceType))
                .newEntity()
                .navigate();
    }

    @Subscribe("sourcesDataGrid.createApi")
    public void onSourcesDataGridCreateApi(final ActionPerformedEvent event) {

    }
}