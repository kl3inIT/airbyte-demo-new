package com.company.airbyte.view.source.fragment;

import com.company.airbyte.dto.source.file.SSH_SCP_SFTP_ProtocolDTO;
import com.company.airbyte.dto.source.file.AzBlobAzureBlobStorageDTO;
import com.company.airbyte.dto.source.file.GCSGoogleCloudStorageDTO;
import com.company.airbyte.dto.source.file.HTTPSPublicWebDTO;
import com.company.airbyte.dto.source.file.S3AmazonWebServicesDTO;
import com.company.airbyte.dto.source.file.SourceFileDTO;
import com.company.airbyte.dto.source.file.StorageProviderType;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

    @ViewComponent
    private InstanceContainer<SourceFileDTO> sourceFileDc;
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

    @Autowired
    private Metadata metadata;

    public void setItem(SourceFileDTO item) {
        super.setItem(item);
        sourceFileDc.setItem(item);
        initializeChildContainers(item);
        visibleFieldsByProvider(item.getProvider(), false);
    }

    public SourceFileDTO getItem() {
        return sourceFileDc.getItemOrNull();
    }

    private void initializeChildContainers(SourceFileDTO item) {
        if (item == null) return;

        StorageProviderType provider = item.getProvider();
        if (provider != null) {
            switch (provider) {
                case S3:
                    if (s3Dc.getItemOrNull() == null) {
                        s3Dc.setItem(metadata.create(S3AmazonWebServicesDTO.class));
                    }
                    break;
                case GCS:
                    if (gcsDc.getItemOrNull() == null) {
                        gcsDc.setItem(metadata.create(GCSGoogleCloudStorageDTO.class));
                    }
                    break;
                case AZ_BLOB:
                    if (azBlobDc.getItemOrNull() == null) {
                        azBlobDc.setItem(metadata.create(AzBlobAzureBlobStorageDTO.class));
                    }
                    break;
                case HTTPS:
                    if (httpsDc.getItemOrNull() == null) {
                        httpsDc.setItem(metadata.create(HTTPSPublicWebDTO.class));
                    }
                    break;
                case SSH, SFTP, SCP:
                    if (sshLikeDc.getItemOrNull() == null) {
                        sshLikeDc.setItem(metadata.create(SSH_SCP_SFTP_ProtocolDTO.class));
                    }
                    break;
            }
        }
    }

    @Subscribe(id = "sourceFileDc", target = Target.DATA_CONTAINER)
    public void onRootPropertyChange(InstanceContainer.ItemPropertyChangeEvent<SourceFileDTO> e) {
        if ("provider".equals(e.getProperty())) {
            StorageProviderType provider = StorageProviderType.fromId(e.getValue().toString());
            visibleFieldsByProvider(provider, true);
        }
    }

    private void visibleFieldsByProvider(StorageProviderType provider, boolean shouldReset) {
        hideAllProviderForms();
        providerFormsBox.setVisible(false);

        if (provider == null) return;
        if (shouldReset) {
            resetSourceExceptProvider();
        }
        initializeChildContainers(sourceFileDc.getItemOrNull());

        providerFormsBox.setVisible(true);
        switch (provider) {
            case S3:
                s3Form.setVisible(true);
                break;
            case GCS:
                gcsForm.setVisible(true);
                break;
            case AZ_BLOB:
                azBlobForm.setVisible(true);
                break;
            case HTTPS:
                httpsForm.setVisible(true);
                break;
            case SSH, SFTP, SCP:
                sshLikeForm.setVisible(true);
                break;
            default:
                break;
        }
    }

    private void resetSourceExceptProvider() {
        SourceFileDTO cur = sourceFileDc.getItemOrNull();
        if (cur != null) {
            StorageProviderType type = cur.getProvider();
            SourceFileDTO fresh = metadata.create(SourceFileDTO.class);
            fresh.setProvider(type);
            sourceFileDc.setItem(fresh);

            initializeChildContainers(fresh);
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
