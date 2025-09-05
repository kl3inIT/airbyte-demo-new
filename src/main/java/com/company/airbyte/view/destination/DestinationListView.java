package com.company.airbyte.view.destination;

import com.company.airbyte.entity.Destination;
import com.company.airbyte.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "destinations", layout = MainView.class)
@ViewController(id = "Destination.list")
@ViewDescriptor(path = "destination-list-view.xml")
@LookupComponent("destinationsDataGrid")
@DialogMode(width = "64em")
public class DestinationListView extends StandardListView<Destination> {
}