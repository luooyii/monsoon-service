package southwest.monsoon.module.common.web.trim;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.Timestamp;

/**
 * @version 6/9/2021
 */
@Configuration
public class RequestBodyTrim {

    /**
     * remove json value space
     *
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return new Jackson2ObjectMapperBuilderCustomizer() {
            @Override
            public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
                jacksonObjectMapperBuilder.serializerByType(Long.class, new CustomLongSerialize());
                jacksonObjectMapperBuilder.serializerByType(Long.TYPE, new CustomLongSerialize());
                jacksonObjectMapperBuilder.serializerByType(Timestamp.class, new JsonSerializer<Timestamp>() {
                    @Override
                    public void serialize(Timestamp value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
                        gen.writeNumber(value.getTime());
                    }
                });
                // trim所有json字段
                jacksonObjectMapperBuilder.deserializerByType(String.class, new StdScalarDeserializer<>(String.class) {
                    @Override
                    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                        return StringUtils.trimWhitespace(jsonParser.getValueAsString());
                    }
                });
            }
        };
    }

    /**
     * 解决Long精度损失问题，如果超过 [-2^53 - 1, 2^53 - 1] ，Long转为String
     */
    class CustomLongSerialize extends JsonSerializer<Long> {
        @Override
        public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value == null) {
                gen.writeNull();
            } else {
                if (value > 9007199254740991L || value < -9007199254740991L) {
                    gen.writeString(String.valueOf(value));
                } else {
                    gen.writeNumber(value);
                }
            }
        }
    }
}