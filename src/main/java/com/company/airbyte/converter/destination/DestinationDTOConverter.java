package com.company.airbyte.converter.destination;

import com.company.airbyte.dto.destination.DestinationDTO;
import com.company.airbyte.utils.EntitySerializationUtils;
import io.jmix.core.EntitySerialization;
import io.jmix.core.EntitySerializationOption;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.postgresql.util.PGobject;

import java.sql.SQLException;

@Converter(autoApply = true)
public class DestinationDTOConverter implements AttributeConverter<DestinationDTO, PGobject> {

    private EntitySerialization es() {
        return EntitySerializationUtils.getEntitySerialization();
    }

    @Override
    public PGobject convertToDatabaseColumn(DestinationDTO dto) {
        if (dto == null) {
            return null;
        }
        try {
            PGobject pgObject = new PGobject();
            pgObject.setType("jsonb");
            String json = es().toJson(dto, null, EntitySerializationOption.SERIALIZE_NULLS);
            pgObject.setValue(json);
            return pgObject;
        } catch (SQLException ex) {
            throw new IllegalArgumentException("Cannot convert " + dto + " to JSON", ex);
        }
    }

    @Override
    public DestinationDTO convertToEntityAttribute(PGobject dbData) {
        if (dbData == null || dbData.getValue() == null) {
            return null;
        }
        String json = dbData.getValue();
        try {
            return es().entityFromJson(json, null, EntitySerializationOption.SERIALIZE_NULLS);
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Cannot convert JSON " + dbData + " to DestinationDTO", ex);
        }
    }
}
