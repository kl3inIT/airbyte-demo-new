package com.company.airbyte.view.destination.fragment;

import com.company.airbyte.dto.destination.s3.DestinationS3DTO;
import com.company.airbyte.dto.destination.s3.DestinationS3OutputFormatType;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.jmix.core.Metadata;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.fragmentrenderer.FragmentRenderer;
import io.jmix.flowui.fragmentrenderer.RendererItemContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import org.springframework.beans.factory.annotation.Autowired;

@FragmentDescriptor("destination-s3-fragment.xml")
@RendererItemContainer("destinationS3Dc")
public class DestinationS3Fragment extends FragmentRenderer<VerticalLayout, DestinationS3DTO> {

//    @Autowired
//    private Metadata metadata;
//
//    @ViewComponent
//    private InstanceContainer<DestinationS3DTO> destinationS3Dc;
//
//    @ViewComponent
//    private FormLayout s3CsvForm;
//
//    @ViewComponent
//    private FormLayout s3JsonLinesForm;
//
//    private DestinationS3DTO item;
//
//    public void setItem(DestinationS3DTO item) {
//        this.item = item;
//        destinationS3Dc.setItem(item);
//        updateFormatVisibility();
//    }
//
//    public DestinationS3DTO getItem() {
//        return destinationS3Dc.getItem();
//    }
//
//    @Subscribe("destinationS3Dc")
//    public void onDestinationS3DcItemChange(InstanceContainer.ItemChangeEvent<DestinationS3DTO> event) {
//        updateFormatVisibility();
//    }
//
//    private void updateFormatVisibility() {
//        DestinationS3DTO dto = destinationS3Dc.getItem();
//        if (dto != null && dto.getFormat() != null) {
//            switch (dto.getFormat()) {
//                case CSV:
//                    s3CsvForm.setVisible(true);
//                    s3JsonLinesForm.setVisible(false);
//                    break;
//                case JSON:
//                    s3CsvForm.setVisible(false);
//                    s3JsonLinesForm.setVisible(true);
//                    break;
//                default:
//                    s3CsvForm.setVisible(false);
//                    s3JsonLinesForm.setVisible(false);
//                    break;
//            }
//        } else {
//            s3CsvForm.setVisible(false);
//            s3JsonLinesForm.setVisible(false);
//        }
//    }
}