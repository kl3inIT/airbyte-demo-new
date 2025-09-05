package com.company.airbyte.view.destination;

import com.airbyte.api.models.shared.DestinationResponse;
import com.company.airbyte.dto.destination.s3.DestinationS3DTO;
import com.company.airbyte.entity.DataFormat;
import com.company.airbyte.entity.Destination;
import com.company.airbyte.entity.DestinationType;
import com.company.airbyte.service.AirbyteService;
import com.company.airbyte.view.destination.fragment.DestinationS3Fragment;
import com.company.airbyte.view.main.MainView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import io.jmix.core.Metadata;
import io.jmix.flowui.Fragments;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Route(value = "destinations/:id", layout = MainView.class)
@ViewController(id = "Destination.detail")
@ViewDescriptor(path = "destination-detail-view.xml")
@EditedEntityContainer("destinationDc")
public class DestinationDetailView extends StandardDetailView<Destination> {

    @Autowired
    private Metadata metadata;

    private DestinationType requestedType;

    @ViewComponent
    private VerticalLayout destinationDetailVbox;

    @Autowired
    private Fragments fragments;

    @Autowired
    private AirbyteService airbyteService;

    private DestinationS3Fragment destinationS3Fragment;
    @Autowired
    private Notifications notifications;

    @ViewComponent
    private InstanceContainer<DestinationS3DTO> destinationS3dc;

    @Subscribe
    public void onQueryParametersChange(QueryParametersChangeEvent event) {
        requestedType = event.getQueryParameters()
                .getSingleParameter("type")
                .map(this::safeParseDestinationType)
                .orElse(null);
    }

    private DestinationType safeParseDestinationType(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return DestinationType.fromId(raw);
        } catch (Exception ignored) {
            return null;
        }
    }

    @Subscribe
    public void onInitEntity(final InitEntityEvent<Destination> event) {
        Destination destination = event.getEntity();

        if (requestedType != null) {
            destination.setDestinationType(requestedType);

            switch (requestedType) {
                case FILE: {
                    DestinationS3DTO destinationS3 = metadata.create(DestinationS3DTO.class);
                    destinationS3dc.setItem(destinationS3);
                    destination.setName("S3");
                    destination.setDestinationType(DestinationType.FILE);
                    destination.setConfiguration(destinationS3);
                    break;
                }
                case DATABASE: {
                    // TODO: Implement database destination details
                    break;
                }
                default: {
                    // TODO: Handle unknown destination type
                }
            }
        }
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        destinationDetailVbox.removeAll();

        DestinationType destinationType = getEditedEntity().getDestinationType();
        if (destinationType != null) {
            switch (destinationType) {
                case FILE: {
                    destinationS3Fragment = fragments.create(this, DestinationS3Fragment.class);

                    DestinationS3DTO destinationS3DTO =
                            (getEditedEntity().getConfiguration() instanceof DestinationS3DTO)
                                    ? (DestinationS3DTO) getEditedEntity().getConfiguration()
                                    : null;
                    if (destinationS3DTO == null) {
                        destinationS3DTO = destinationS3dc.getItemOrNull();
                        if (destinationS3DTO == null) {
                            destinationS3DTO = metadata.create(DestinationS3DTO.class);
                        }
                        getEditedEntity().setConfiguration(destinationS3DTO);
                    }
                    if (destinationS3dc.getItemOrNull() != destinationS3DTO) {
                        destinationS3dc.setItem(destinationS3DTO);
                    }

                    destinationS3Fragment.setItem(destinationS3DTO);
                    destinationDetailVbox.add(destinationS3Fragment);
                    break;
                }
                case DATABASE: {
                    // TODO: Implement database destination fragment
                }
            }
        }
    }

    @Subscribe
    public void onBeforeSave(final BeforeSaveEvent event) {
        Destination destination = getEditedEntity();
        DestinationType destinationType = getEditedEntity().getDestinationType();
        if (destinationType != null) {
            switch (destinationType) {
                case FILE: {
                    try {
                        DestinationS3DTO current = destinationS3dc.getItemOrNull();
                        if (current != null && destination.getConfiguration() != current) {
                            destination.setConfiguration(current);
                        }

                        // Airbyte API call đang để comment cho bạn debug UI
//                        DestinationResponse resp = airbyteService.upsertDestinationOnAirbyte(destination);
//                        if (resp.destinationId() != null) {
//                            destination.setDestinationId(UUID.fromString(resp.destinationId()));
//                        }
//                        if (resp.workspaceId() != null) {
//                            destination.setWorkspaceId(UUID.fromString(resp.workspaceId()));
//                        }

                        destination.setDataFormat(DataFormat.TABLE);
                        getViewData().getDataContext().setModified(destination, true);

                    } catch (Exception ex) {
                        // Giữ nguyên ý đồ của bạn: không preventSave, chỉ cảnh báo
//                        event.preventSave();
                        notifications.create("Airbyte create destination failed")
                                .withType(Notifications.Type.ERROR).show();
                    }
                    break;
                }
                case DATABASE: {
                    // TODO: Implement database destination save
                }
            }
        }
    }
}
