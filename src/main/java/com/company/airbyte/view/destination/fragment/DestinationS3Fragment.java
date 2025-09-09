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

        DestinationS3OutputFormat existing = null;
        // Khôi phục từ outputFormat nếu có
        if (item != null) {
            existing = item.getOutputFormat();
        }
        if (existing != null) {
            if (existing instanceof DestinationS3CSVCommaSeparatedValuesDTO csv) {
                csvDc.setItem(csv);
                if (item.getFormat() != DestinationS3OutputFormatType.CSV) item.setFormat(DestinationS3OutputFormatType.CSV);
            } else if (existing instanceof DestinationS3JSONLinesNewlineDelimitedJSONDTO jsonl) {
                jsonlDc.setItem(jsonl);
                if (item.getFormat() != DestinationS3OutputFormatType.JSON) item.setFormat(DestinationS3OutputFormatType.JSON);
            } else if (existing instanceof DestinationS3ParquetColumnarStorageDTO parquet) {
                parquetDc.setItem(parquet);
                if (item.getFormat() != DestinationS3OutputFormatType.PARQUET) item.setFormat(DestinationS3OutputFormatType.PARQUET);
            } else if (existing instanceof DestinationS3AvroApacheAvroDTO avro) {
                avroDc.setItem(avro);
                if (item.getFormat() != DestinationS3OutputFormatType.AVRO) item.setFormat(DestinationS3OutputFormatType.AVRO);
            }
        }


        DestinationS3OutputFormatType type = null;
        if (item != null) {
            type = item.getFormat();
        }
        initializeChildContainers(type);
        visibleFieldsByFormat(type);

        DestinationS3AvroApacheAvroDTO avro = avroDc.getItemOrNull();
        if (avro != null) {
            Object codec = avro.getCodec();
            // Khôi phục codec con nếu đã có formatCodec
            DestinationS3AvroCompressionCodecDTO existedCodec = avro.getFormatCodec();
            if (existedCodec instanceof DestinationS3AvroDeflateDTO) {
                avroDeflateDc.setItem((DestinationS3AvroDeflateDTO) existedCodec);
            } else if (existedCodec instanceof DestinationS3AvroXzDTO) {
                avroXzDc.setItem((DestinationS3AvroXzDTO) existedCodec);
            } else if (existedCodec instanceof DestinationS3AvroZstandardDTO) {
                avroZstdDc.setItem((DestinationS3AvroZstandardDTO) existedCodec);
            }
            initializeAvroCodecChild(codec);
            visibleAvroCodecForms(codec);
            attachFormatCodecToAvro(codec);
        } else {
            hideAllAvroCodecForms();
        }
    }

    // Lắng nghe đổi format
    @Subscribe(id = "destinationS3dc", target = Target.DATA_CONTAINER)
    public void onS3ItemPropertyChange(final InstanceContainer.ItemPropertyChangeEvent<DestinationS3DTO> e) {
        if ("format".equals(e.getProperty())) {
            DestinationS3OutputFormatType type = null;
            if (e.getValue() instanceof DestinationS3OutputFormatType) {
                type = (DestinationS3OutputFormatType) e.getValue();
            } else if (e.getValue() != null) {
                type = DestinationS3OutputFormatType.fromId(e.getValue().toString());
            }
            resetFormatSpecific();
            initializeChildContainers(type);
            visibleFieldsByFormat(type);
        }
    }

    // Lắng nghe đổi codec Avro
    @Subscribe(id = "avroDc", target = Target.DATA_CONTAINER)
    public void onAvroItemPropertyChange(final InstanceContainer.ItemPropertyChangeEvent<DestinationS3AvroApacheAvroDTO> e) {
        if ("codec".equals(e.getProperty())) {
            Object codecVal = e.getValue();
            initializeAvroCodecChild(codecVal);
            visibleAvroCodecForms(codecVal);
            attachFormatCodecToAvro(codecVal);
        }
    }

    private void initializeChildContainers(DestinationS3OutputFormatType type) {
        if (type == null) return;
        DestinationS3DTO root = destinationS3dc.getItemOrNull();
        switch (type) {
            case CSV:
                if (csvDc.getItemOrNull() == null) {
                    csvDc.setItem(metadata.create(DestinationS3CSVCommaSeparatedValuesDTO.class));
                }
                if (root != null) root.setOutputFormat(csvDc.getItem());
                break;
            case JSON:
                if (jsonlDc.getItemOrNull() == null) {
                    jsonlDc.setItem(metadata.create(DestinationS3JSONLinesNewlineDelimitedJSONDTO.class));
                }
                if (root != null) root.setOutputFormat(jsonlDc.getItem());
                break;
            case PARQUET:
                if (parquetDc.getItemOrNull() == null) {
                    parquetDc.setItem(metadata.create(DestinationS3ParquetColumnarStorageDTO.class));
                }
                if (root != null) root.setOutputFormat(parquetDc.getItem());
                break;
            case AVRO:
                if (avroDc.getItemOrNull() == null) {
                    avroDc.setItem(metadata.create(DestinationS3AvroApacheAvroDTO.class));
                }
                if (root != null) root.setOutputFormat(avroDc.getItem());
                break;
        }
    }

    private void visibleFieldsByFormat(DestinationS3OutputFormatType type) {
        hideAllFormatForms();
        if (type == null) return;
        switch (type) {
            case CSV:
                if (csvForm != null) csvForm.setVisible(true);
                break;
            case JSON:
                if (jsonlForm != null) jsonlForm.setVisible(true);
                break;
            case PARQUET:
                if (parquetForm != null) parquetForm.setVisible(true);
                break;
            case AVRO:
                if (avroForm != null) avroForm.setVisible(true);
                // sẽ hiển thị codec theo initialize/visible codec riêng
                break;
        }
    }

    private void resetFormatSpecific() {
        // Không xóa dữ liệu người dùng, chỉ ẩn form và để initialize lại nếu cần.
        hideAllFormatForms();
        hideAllAvroCodecForms();
        DestinationS3DTO root = destinationS3dc.getItemOrNull();
        if (root != null) root.setOutputFormat(null);
    }

    private void hideAllFormatForms() {
        if (csvForm != null) csvForm.setVisible(false);
        if (jsonlForm != null) jsonlForm.setVisible(false);
        if (parquetForm != null) parquetForm.setVisible(false);
        if (avroForm != null) avroForm.setVisible(false);
    }

    private DestinationS3CompressionCodec asCompressionEnum(Object codecVal) {
        if (codecVal instanceof DestinationS3CompressionCodec) {
            return (DestinationS3CompressionCodec) codecVal;
        }
        if (codecVal != null) {
            try {
                return DestinationS3CompressionCodec.valueOf(codecVal.toString().toUpperCase());
            } catch (IllegalArgumentException ignored) {
                return null;
            }
        }
        return null;
    }

    private void initializeAvroCodecChild(Object codecVal) {
        DestinationS3CompressionCodec codec = asCompressionEnum(codecVal);
        if (codec == null) return;
        switch (codec) {
            case DEFLATE:
                if (avroDeflateDc.getItemOrNull() == null) {
                    avroDeflateDc.setItem(metadata.create(DestinationS3AvroDeflateDTO.class));
                }
                break;
            case XZ:
                if (avroXzDc.getItemOrNull() == null) {
                    avroXzDc.setItem(metadata.create(DestinationS3AvroXzDTO.class));
                }
                break;
            case ZSTANDARD:
                if (avroZstdDc.getItemOrNull() == null) {
                    avroZstdDc.setItem(metadata.create(DestinationS3AvroZstandardDTO.class));
                }
                break;
            default:
                break;
        }
    }

    private void visibleAvroCodecForms(Object codecVal) {
        DestinationS3CompressionCodec codec = asCompressionEnum(codecVal);
        boolean showDeflate = codec == DestinationS3CompressionCodec.DEFLATE;
        boolean showXz = codec == DestinationS3CompressionCodec.XZ;
        boolean showZstd = codec == DestinationS3CompressionCodec.ZSTANDARD;
        if (avroDeflateForm != null) avroDeflateForm.setVisible(showDeflate);
        if (avroXzForm != null) avroXzForm.setVisible(showXz);
        if (avroZstdForm != null) avroZstdForm.setVisible(showZstd);
    }

    private void hideAllAvroCodecForms() {
        if (avroDeflateForm != null) avroDeflateForm.setVisible(false);
        if (avroXzForm != null) avroXzForm.setVisible(false);
        if (avroZstdForm != null) avroZstdForm.setVisible(false);
    }

    private void attachFormatCodecToAvro(Object codecVal) {
        DestinationS3AvroApacheAvroDTO avro = avroDc.getItemOrNull();
        if (avro == null) return;
        DestinationS3CompressionCodec codec = asCompressionEnum(codecVal);
        if (codec == null) {
            avro.setFormatCodec(null);
            return;
        }
        switch (codec) {
            case DEFLATE:
                if (avroDeflateDc.getItemOrNull() != null) {
                    avro.setFormatCodec(avroDeflateDc.getItem());
                }
                break;
            case XZ:
                if (avroXzDc.getItemOrNull() != null) {
                    avro.setFormatCodec(avroXzDc.getItem());
                }
                break;
            case ZSTANDARD:
                if (avroZstdDc.getItemOrNull() != null) {
                    avro.setFormatCodec(avroZstdDc.getItem());
                }
                break;
            default:
                avro.setFormatCodec(null);
        }
    }
}
