package com.company.airbyte.converter.destination;

import com.company.airbyte.dto.destination.DestinationDTO;
import com.company.airbyte.utils.EntitySerializationUtils;
import io.jmix.core.EntitySerialization;
import io.jmix.core.EntitySerializationOption;
import io.jmix.core.metamodel.annotation.DatatypeDef;
import io.jmix.core.metamodel.annotation.Ddl;
import io.jmix.core.metamodel.datatype.Datatype;

import javax.annotation.Nullable;
import java.text.ParseException;
import java.util.Locale;

@DatatypeDef(
        id = "destinationDTO",
        javaClass = DestinationDTO.class,
        defaultForClass = true
)
@Ddl(dbms = "postgres", value = "jsonb")
public class DestinationDTODataType implements Datatype<DestinationDTO> {

    private EntitySerialization es() {
        return EntitySerializationUtils.getEntitySerialization();
    }

    @Override
    public String format(@Nullable Object value) {
        if (!(value instanceof DestinationDTO dto)) {
            return "";
        }
        try {
            return es().toJson(dto, null, EntitySerializationOption.SERIALIZE_NULLS);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Failed to format DestinationDTO to JSON", e);
        }
    }

    @Override
    public String format(@Nullable Object value, Locale locale) {
        return format(value);
    }

    @Override
    public DestinationDTO parse(@Nullable String value) throws ParseException {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return es().entityFromJson(value, null, EntitySerializationOption.SERIALIZE_NULLS);
        } catch (RuntimeException e) {
            ParseException pe = new ParseException("Invalid DestinationDTO JSON", 0);
            pe.initCause(e);
            throw pe;
        }
    }

    @Override
    public DestinationDTO parse(@Nullable String value, Locale locale) throws ParseException {
        return parse(value);
    }
}
