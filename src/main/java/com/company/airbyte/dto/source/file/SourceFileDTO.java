package com.company.airbyte.dto.source.file;

import com.airbyte.api.models.shared.File;
import com.airbyte.api.models.shared.FileFormat;
import com.airbyte.api.models.shared.StorageProvider;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.Optional;

@JmixEntity
public class SourceFileDTO {

    private String datasetName;

    private Optional<? extends FileFormat> format;

    private StorageProvider provider;

    private String readerOptions;

//    private File sourceType;

    private String url;

}