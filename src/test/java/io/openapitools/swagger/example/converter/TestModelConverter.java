package io.openapitools.swagger.example.converter;

import io.openapitools.swagger.example.model.ConversionParameter;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;

import java.util.Iterator;

public class TestModelConverter implements ModelConverter {

    public Schema resolve(AnnotatedType annotatedType, ModelConverterContext modelConverterContext, Iterator<ModelConverter> iterator) {
        if (annotatedType.getType().getTypeName().equals(String.format("[simple type, class %s]", ConversionParameter.class.getName()))) {
            return new Schema().type("string").format("date");
        }

        if (iterator.hasNext()) {
            return iterator.next().resolve(annotatedType, modelConverterContext, iterator);
        } else {
            return null;
        }
    }
}
