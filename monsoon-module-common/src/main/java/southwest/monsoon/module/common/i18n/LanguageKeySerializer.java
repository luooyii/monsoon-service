package southwest.monsoon.module.common.i18n;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;
import southwest.monsoon.module.common.web.result.exception.WebException;

import java.io.IOException;
import java.lang.reflect.Field;

@Slf4j
public class LanguageKeySerializer extends JsonSerializer<Object> {
    static MultiLanguageService languageService;

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        try {
            Field[] fields = value.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);

                Object fieldValue = field.get(value);
                gen.writeObjectField(field.getName(), fieldValue);

                if (field.isAnnotationPresent(LanguageKey.class)) {
                    String translation = languageService.parse(fieldValue);
                    gen.writeObjectField(field.getName() + "Val", translation);
                }
            }
        } catch (Exception e) {
            throw WebException.cause(e);
        }

        gen.writeEndObject();
    }
}
