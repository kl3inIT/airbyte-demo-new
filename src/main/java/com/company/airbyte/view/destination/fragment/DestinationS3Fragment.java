package com.company.airbyte.view.destination.fragment;

import com.company.airbyte.dto.destination.s3.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.jmix.core.Metadata;
import io.jmix.flowui.component.combobox.JmixComboBox;
import io.jmix.flowui.component.formlayout.JmixFormLayout;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.fragmentrenderer.FragmentRenderer;
import io.jmix.flowui.fragmentrenderer.RendererItemContainer;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.Target;
import io.jmix.flowui.view.ViewComponent;
import org.springframework.beans.factory.annotation.Autowired;

@FragmentDescriptor("destination-s3-fragment.xml")
@RendererItemContainer("destinationS3dc")
public class DestinationS3Fragment extends FragmentRenderer<VerticalLayout, DestinationS3DTO> {

    @Autowired
    private Metadata metadata;

    @ViewComponent
    private InstanceContainer<DestinationS3DTO> destinationS3dc;
    @ViewComponent
    private InstanceContainer<DestinationS3CSVCommaSeparatedValuesDTO> csvDc;
    @ViewComponent
    private InstanceContainer<DestinationS3JSONLinesNewlineDelimitedJSONDTO> jsonlDc;
    @ViewComponent
    private InstanceContainer<DestinationS3ParquetColumnarStorageDTO> parquetDc;
    @ViewComponent
    private InstanceContainer<DestinationS3AvroApacheAvroDTO> avroDc;

    @ViewComponent
    private InstanceContainer<DestinationS3AvroDeflateDTO> avroDeflateDc;
    @ViewComponent
    private InstanceContainer<DestinationS3AvroXzDTO> avroXzDc;
    @ViewComponent
    private InstanceContainer<DestinationS3AvroZstandardDTO> avroZstdDc;

    @ViewComponent
    private JmixFormLayout csvForm;
    @ViewComponent
    private JmixFormLayout jsonlForm;
    @ViewComponent
    private JmixFormLayout parquetForm;
    @ViewComponent
    private JmixFormLayout avroForm;

    @ViewComponent
    private JmixFormLayout avroDeflateForm;
    @ViewComponent
    private JmixFormLayout avroXzForm;
    @ViewComponent
    private JmixFormLayout avroZstdForm;

    @ViewComponent
    private JmixComboBox<DestinationS3OutputFormatType> formatField;
    @ViewComponent
    private JmixComboBox<?> avroCodecField;

    @Override
    public void setItem(DestinationS3DTO item) {
        super.setItem(item);
        destinationS3dc.setItem(item);

        ensureChildFor(item != null ? item.getFormat() : null);
        updateFormatForms(item != null ? item.getFormat() : null);

        DestinationS3AvroApacheAvroDTO avro = avroDc.getItemOrNull();
        if (avro != null) {
            ensureAvroCodecItem(avro.getCodec());
            updateAvroCodecForms(avro.getCodec());
        }
    }

    // Khi đổi format
    @Subscribe(id = "destinationS3dc", target = Target.DATA_CONTAINER)
    public void onS3ItemPropertyChange(final InstanceContainer.ItemPropertyChangeEvent<DestinationS3DTO> e) {
        if ("format".equals(e.getProperty())) {
            DestinationS3OutputFormatType type = null;
            if (e.getValue() instanceof DestinationS3OutputFormatType) {
                type = (DestinationS3OutputFormatType) e.getValue();
            } else if (e.getValue() != null) {
                type = DestinationS3OutputFormatType.fromId(e.getValue().toString());
            }
            ensureChildFor(type);
            updateFormatForms(type);
        }
    }

    // Khi đổi codec Avro
    @Subscribe(id = "avroDc", target = Target.DATA_CONTAINER)
    public void onAvroItemPropertyChange(final InstanceContainer.ItemPropertyChangeEvent<DestinationS3AvroApacheAvroDTO> e) {
        if ("codec".equals(e.getProperty())) {
            Object codecVal = e.getValue();
            ensureAvroCodecItem(codecVal);
            updateAvroCodecForms(codecVal);
            attachFormatCodecToAvro(codecVal);
        }
    }

    private void updateFormatForms(DestinationS3OutputFormatType type) {
        boolean isCsv = type == DestinationS3OutputFormatType.CSV;
        boolean isJsonl = type == DestinationS3OutputFormatType.JSON;
        boolean isParquet = type == DestinationS3OutputFormatType.PARQUET;
        boolean isAvro = type == DestinationS3OutputFormatType.AVRO;

        if (csvForm != null) csvForm.setVisible(isCsv);
        if (jsonlForm != null) jsonlForm.setVisible(isJsonl);
        if (parquetForm != null) parquetForm.setVisible(isParquet);
        if (avroForm != null) avroForm.setVisible(isAvro);
    }

    private void ensureChildFor(DestinationS3OutputFormatType type) {
        if (type == null) return;
        switch (type) {
            case CSV -> ensure(csvDc, DestinationS3CSVCommaSeparatedValuesDTO.class);
            case JSON -> ensure(jsonlDc, DestinationS3JSONLinesNewlineDelimitedJSONDTO.class);
            case PARQUET -> ensure(parquetDc, DestinationS3ParquetColumnarStorageDTO.class);
            case AVRO -> ensure(avroDc, DestinationS3AvroApacheAvroDTO.class);
        }
    }

    private void ensureAvroCodecItem(Object codecVal) {
        if (codecVal == null) return;
        String v = codecVal.toString().toUpperCase();

        switch (v) {
            case "DEFLATE" -> ensure(avroDeflateDc, DestinationS3AvroDeflateDTO.class);
            case "XZ" -> ensure(avroXzDc, DestinationS3AvroXzDTO.class);
            case "ZSTANDARD", "ZSTD" -> ensure(avroZstdDc, DestinationS3AvroZstandardDTO.class);
            default -> { /* nothing */ }
        }
    }

    private void updateAvroCodecForms(Object codecVal) {
        boolean showDeflate = false, showXz = false, showZstd = false;
        if (codecVal != null) {
            String v = codecVal.toString().toUpperCase();
            showDeflate = "DEFLATE".equals(v);
            showXz = "XZ".equals(v);
            showZstd = "ZSTANDARD".equals(v) || "ZSTD".equals(v);
        }
        if (avroDeflateForm != null) avroDeflateForm.setVisible(showDeflate);
        if (avroXzForm != null) avroXzForm.setVisible(showXz);
        if (avroZstdForm != null) avroZstdForm.setVisible(showZstd);
    }

    private void attachFormatCodecToAvro(Object codecVal) {
        DestinationS3AvroApacheAvroDTO avro = avroDc.getItemOrNull();
        if (avro == null || codecVal == null) return;

        String v = codecVal.toString().toUpperCase();
        if ("DEFLATE".equals(v) && avroDeflateDc.getItemOrNull() != null) {
            avro.setFormatCodec(avroDeflateDc.getItem());
        } else if ("XZ".equals(v) && avroXzDc.getItemOrNull() != null) {
            avro.setFormatCodec(avroXzDc.getItem());
        } else if (("ZSTANDARD".equals(v) || "ZSTD".equals(v)) && avroZstdDc.getItemOrNull() != null) {
            avro.setFormatCodec(avroZstdDc.getItem());
        } else {
            // codec khác -> xóa để JSON gọn
            avro.setFormatCodec(null);
        }
    }

    private <T> void ensure(InstanceContainer<T> dc, Class<T> clazz) {
        if (dc.getItemOrNull() == null) {
            dc.setItem(metadata.create(clazz));
        }
    }
}
