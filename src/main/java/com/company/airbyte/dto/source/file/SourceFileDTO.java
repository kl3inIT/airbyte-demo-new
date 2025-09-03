package com.company.airbyte.dto.source.file;

import com.airbyte.api.models.shared.FileFormat;
import com.airbyte.api.models.shared.StorageProvider;
import com.company.airbyte.dto.source.SourceDTO;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.Optional;

@JmixEntity
public class SourceFileDTO extends SourceDTO {

    private String datasetName;

    private Optional<? extends FileFormat> format;

    private StorageProvider provider;

    private String readerOptions;

//    private File sourceType;

    private String url;

}