package com.company.airbyte.view.source;

import com.company.airbyte.entity.Source;
import com.company.airbyte.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "sources/:id", layout = MainView.class)
@ViewController(id = "Source.detail")
@ViewDescriptor(path = "source-detail-view.xml")
@EditedEntityContainer("sourceDc")
public class SourceDetailView extends StandardDetailView<Source> {
}