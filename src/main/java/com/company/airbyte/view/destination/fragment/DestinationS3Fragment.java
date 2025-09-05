package com.company.airbyte.view.destination.fragment;

import com.company.airbyte.dto.destination.s3.DestinationS3DTO;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.fragmentrenderer.FragmentRenderer;

@FragmentDescriptor("destination-s3-fragment.xml")
public class DestinationS3Fragment extends FragmentRenderer<VerticalLayout, DestinationS3DTO> {
}