package ru.gozhan.pronotesapi.test.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class JsonUtil {

    private static final ObjectMapper UNRESTRICTED_MAPPER_WITH_NULLS =
            createMapper(JsonInclude.Include.ALWAYS);

    private static final ObjectMapper UNRESTRICTED_MAPPER_WITHOUT_NULLS =
            createMapper(JsonInclude.Include.NON_NULL);

    private static ObjectMapper createMapper(
            final JsonInclude.Include includePolicy
    ) {
        ObjectMapper mapper = new ObjectMapper()
                .setSerializationInclusion(includePolicy)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
                    @Override
                    public JsonProperty.Access findPropertyAccess(
                            final Annotated a
                    ) {
                        return JsonProperty.Access.AUTO;
                    }
                });

        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, new ToStringSerializer());
        mapper.registerModule(module);

        return mapper;
    }

    public static String toJsonWithNulls(final Object object) {
        try {
            return UNRESTRICTED_MAPPER_WITH_NULLS.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error while serialize object to json with null field",
                    e
            );
        }
    }

    public static String toJsonWithoutNulls(final Object object) {
        try {
            return UNRESTRICTED_MAPPER_WITHOUT_NULLS.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error while serialize object to json without null field",
                    e
            );
        }
    }

}
