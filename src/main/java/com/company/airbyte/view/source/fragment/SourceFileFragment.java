package com.company.airbyte.view.source.fragment;

import com.company.airbyte.dto.source.SourceDatabaseDTO;
import com.company.airbyte.dto.source.common.SSH_SCP_SFTP_ProtocolDTO;
import com.company.airbyte.dto.source.file.AzBlobAzureBlobStorageDTO;
import com.company.airbyte.dto.source.file.GCSGoogleCloudStorageDTO;
import com.company.airbyte.dto.source.file.HTTPSPublicWebDTO;
import com.company.airbyte.dto.source.file.S3AmazonWebServicesDTO;
import com.company.airbyte.dto.source.file.SourceFileDTO;
import com.company.airbyte.dto.source.file.StorageProviderType;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import io.jmix.core.Metadata;
import io.jmix.flowui.component.formlayout.JmixFormLayout;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.fragmentrenderer.FragmentRenderer;
import io.jmix.flowui.fragmentrenderer.RendererItemContainer;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.Target;
import io.jmix.flowui.view.ViewComponent;
import org.springframework.beans.factory.annotation.Autowired;

@FragmentDescriptor("source-file-fragment.xml")
@RendererItemContainer("sourceFileDc")
public class SourceFileFragment extends FragmentRenderer<VerticalLayout, SourceFileDTO> {

    // Root
    @ViewComponent
    private InstanceContainer<SourceFileDTO> sourceFileDc;

    // Provider DTO containers
    @ViewComponent
    private InstanceContainer<S3AmazonWebServicesDTO> s3Dc;
    @ViewComponent
    private InstanceContainer<GCSGoogleCloudStorageDTO> gcsDc;
    @ViewComponent
    private InstanceContainer<AzBlobAzureBlobStorageDTO> azBlobDc;
    @ViewComponent
    private InstanceContainer<HTTPSPublicWebDTO> httpsDc;
    @ViewComponent
    private InstanceContainer<SSH_SCP_SFTP_ProtocolDTO> sshLikeDc;

    // Provider forms
    @ViewComponent
    private JmixFormLayout s3Form;
    @ViewComponent
    private JmixFormLayout gcsForm;
    @ViewComponent
    private JmixFormLayout azBlobForm;
    @ViewComponent
    private JmixFormLayout httpsForm;
    @ViewComponent
    private JmixFormLayout sshLikeForm;
    @ViewComponent
    private VerticalLayout providerFormsBox;

    // Description + counter
    @ViewComponent
    private TextArea descriptionField; // Vaadin TextArea
    @ViewComponent("descriptionCounter")
    private Component descriptionCounter; // update text via element

    @Autowired
    private Metadata metadata;

    @Override
    public void setItem(SourceFileDTO item) {
        super.setItem(item);
        sourceFileDc.setItem(item);

        // cập nhật counter lần đầu dựa theo giá trị đang có trong field
        updateDescriptionCounter();

        // khi mở bản ghi edit: show đúng provider form
        if (item != null && item.getProvider() != null) {
            visibleFieldsByProvider(item.getProvider());
        } else {
            hideAllProviderForms();
        }
    }

    public SourceFileDTO getItem() {
        return sourceFileDc.getItemOrNull();
    }

    // lắng nghe đổi provider trên data container giống mẫu Database
    @Subscribe(id = "sourceFileDc", target = Target.DATA_CONTAINER)
    public void onRootPropertyChange(InstanceContainer.ItemPropertyChangeEvent<SourceFileDTO> e) {
        if ("provider".equals(e.getProperty())) {
            SourceFileDTO root = sourceFileDc.getItemOrNull();
            visibleFieldsByProvider(root != null ? root.getProvider() : null);
        }
    }

    // đếm ký tự mô tả, không cần InitEvent
    @Subscribe("descriptionField")
    public void onDescriptionValueChange(HasValue.ValueChangeEvent<?> event) {
        updateDescriptionCounter();
    }

    private void updateDescriptionCounter() {
        int len = 0;
        if (descriptionField != null && descriptionField.getValue() != null) {
            len = descriptionField.getValue().length();
        }
        if (descriptionCounter != null) {
            descriptionCounter.getElement().setText(len + "/200");
        }
    }

    private void visibleFieldsByProvider(StorageProviderType provider) {
        hideAllProviderForms();
        if (provider == null) {
            providerFormsBox.setVisible(false);
            return;
        }
        providerFormsBox.setVisible(true);
        switch (provider) {
            case S3 -> {
                if (s3Dc.getItemOrNull() == null) {
                    s3Dc.setItem(metadata.create(S3AmazonWebServicesDTO.class));
                }
                s3Form.setVisible(true);
            }
            case GCS -> {
                if (gcsDc.getItemOrNull() == null) {
                    gcsDc.setItem(metadata.create(GCSGoogleCloudStorageDTO.class));
                }
                gcsForm.setVisible(true);
            }
            case AZ_BLOB -> {
                if (azBlobDc.getItemOrNull() == null) {
                    azBlobDc.setItem(metadata.create(AzBlobAzureBlobStorageDTO.class));
                }
                azBlobForm.setVisible(true);
            }
            case HTTPS -> {
                if (httpsDc.getItemOrNull() == null) {
                    httpsDc.setItem(metadata.create(HTTPSPublicWebDTO.class));
                }
                httpsForm.setVisible(true);
            }
            case SSH, SFTP, SCP -> {
                if (sshLikeDc.getItemOrNull() == null) {
                    sshLikeDc.setItem(metadata.create(SSH_SCP_SFTP_ProtocolDTO.class));
                }
                sshLikeForm.setVisible(true);
            }
            case LOCAL -> {
                // Không có cấu hình bổ sung
            }
        }
    }

    private void hideAllProviderForms() {
        s3Form.setVisible(false);
        gcsForm.setVisible(false);
        azBlobForm.setVisible(false);
        httpsForm.setVisible(false);
        sshLikeForm.setVisible(false);
    }
}
