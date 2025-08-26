package com.company.airbyte.converter.source;

import com.company.airbyte.dto.source.SourceDTO;
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
        id = "sourceDTO",
        javaClass = SourceDTO.class,
        defaultForClass = true
)
@Ddl(dbms = "postgres", value = "jsonb")
public class SourceDTODataType implements Datatype<SourceDTO> {

    private EntitySerialization es() {
        return EntitySerializationUtils.getEntitySerialization();
    }

    @Override
    public String format(@Nullable Object value) {
        if (!(value instanceof SourceDTO dto)) {
            return "";
        }
        try {
            return es().toJson(dto, null, EntitySerializationOption.SERIALIZE_NULLS);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Failed to format SourceDTO to JSON", e);
        }
    }

    @Override
    public String format(@Nullable Object value, Locale locale) {
        return format(value);
    }

    @Override
    public SourceDTO parse(@Nullable String value) throws ParseException {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return es().entityFromJson(value, null, EntitySerializationOption.SERIALIZE_NULLS);
        } catch (RuntimeException e) {
            ParseException pe = new ParseException("Invalid SourceDTO JSON", 0);
            pe.initCause(e);
            throw pe;
        }
    }

    @Override
    public SourceDTO parse(@Nullable String value, Locale locale) throws ParseException {
        return parse(value);
    }
}
