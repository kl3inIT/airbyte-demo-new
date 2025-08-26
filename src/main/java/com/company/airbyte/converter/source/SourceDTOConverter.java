package com.company.airbyte.converter.source;

import com.company.airbyte.dto.source.SourceDTO;
import com.company.airbyte.utils.EntitySerializationUtils;
import io.jmix.core.EntitySerialization;
import io.jmix.core.EntitySerializationOption;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.postgresql.util.PGobject;

import java.sql.SQLException;

@Converter(autoApply = true)
public class SourceDTOConverter implements AttributeConverter<SourceDTO, PGobject> {

    private EntitySerialization es() {
        return EntitySerializationUtils.getEntitySerialization();
    }

    @Override
    public PGobject convertToDatabaseColumn(SourceDTO sourceDTO) {
        if (sourceDTO == null) {
            return null;
        }

        try {
            PGobject pgObject = new PGobject();
            pgObject.setType("jsonb");
            String json = es().toJson(sourceDTO, null, EntitySerializationOption.SERIALIZE_NULLS);
            pgObject.setValue(json);
            return pgObject;
        } catch (SQLException ex) {
            throw new IllegalArgumentException("Cannot convert " + sourceDTO + " to JSON", ex);
        }
    }

    @Override
    public SourceDTO convertToEntityAttribute(PGobject dbData) {
        if (dbData == null || dbData.getValue() == null) {
            return null;
        }
        String json = dbData.getValue();

        try {
            return es().entityFromJson(json, null, EntitySerializationOption.SERIALIZE_NULLS);
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Cannot convert JSON " + dbData + " to SourceDTO", ex);
        }
    }

}
