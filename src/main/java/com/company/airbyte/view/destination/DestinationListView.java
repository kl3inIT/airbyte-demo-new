package com.company.airbyte.view.destination;

import com.company.airbyte.entity.Destination;
import com.company.airbyte.entity.DestinationType;
import com.company.airbyte.view.main.MainView;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;


@Route(value = "destinations", layout = MainView.class)
@ViewController(id = "Destination.list")
@ViewDescriptor(path = "destination-list-view.xml")
@LookupComponent("destinationsDataGrid")
@DialogMode(width = "64em")
public class DestinationListView extends StandardListView<Destination> {

    @Autowired
    private ViewNavigators viewNavigators;

    @Subscribe("destinationsDataGrid.createDb")
    public void onDestinationsDataGridCreateDb(final ActionPerformedEvent event) {
        String destinationType = DestinationType.DATABASE.getId();
        viewNavigators.detailView(this, Destination.class)
                .withQueryParameters(QueryParameters.of("type", destinationType))
                .newEntity()
                .navigate();
    }

    @Subscribe("destinationsDataGrid.createFile")
    public void onDestinationsDataGridCreateFile(final ActionPerformedEvent event) {
        String destinationType = DestinationType.FILE.getId();
        viewNavigators.detailView(this, Destination.class)
                .withQueryParameters(QueryParameters.of("type", destinationType))
                .newEntity()
                .navigate();
    }

    @Subscribe("destinationsDataGrid.createApi")
    public void onDestinationsDataGridCreateApi(final ActionPerformedEvent event) {
        // TODO: Implement API destination creation
    }
}