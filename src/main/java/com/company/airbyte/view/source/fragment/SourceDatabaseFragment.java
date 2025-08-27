package com.company.airbyte.view.source.fragment;

import com.company.airbyte.dto.source.SourceDatabaseDTO;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.fragmentrenderer.FragmentRenderer;
import io.jmix.flowui.fragmentrenderer.RendererItemContainer;

@FragmentDescriptor("source-database-fragment.xml")
@RendererItemContainer("sourceDc")
public class SourceDatabaseFragment extends FragmentRenderer<VerticalLayout, SourceDatabaseDTO> {
}