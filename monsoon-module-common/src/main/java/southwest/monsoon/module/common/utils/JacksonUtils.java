package southwest.monsoon.module.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @version 6/8/2021
 */
@Slf4j
@UtilityClass
public class JacksonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ObjectMapper ingnoreNullMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ingnoreNullMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ingnoreNullMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static boolean isNodeEmpty(JsonNode jsonNode) {
        if (jsonNode != null && jsonNode.size() > 0) {
            return false;
        }
        return true;
    }

    public static JsonNode parseJson(String jsonStr) throws JsonProcessingException {
        if (jsonStr != null) {
            return objectMapper.readTree(jsonStr);
        }
        return null;
    }

    public static <T> T parseJson(String jsonStr, Class<T> clasz) throws JsonProcessingException {
        if (jsonStr != null) {
            return objectMapper.readValue(jsonStr, clasz);
        }
        return null;
    }

    public static <T> T convert(Object fromValue, Class<T> clasz) {
        if (fromValue != null) {
            return objectMapper.convertValue(fromValue, clasz);
        }
        return null;
    }

    public static <T> T parseJson(JsonNode jsonNode, Class<T> clasz) throws IOException {
        if (jsonNode != null) {
            return objectMapper.treeToValue(jsonNode, clasz);
        }
        return null;
    }

    public static String toJsonStr(Object obj) throws IOException {
        return toJsonStr(obj, false);
    }

    public static String toJsonStr(Object obj, boolean isIgnoreNull) throws IOException {
        if (obj == null) {
            return "null";
        }
        if (isCommonType(obj)) {
            return obj.toString();
        }
        if (isIgnoreNull) {
            return ingnoreNullMapper.writeValueAsString(obj);
        } else {
            return objectMapper.writeValueAsString(obj);
        }
    }

    public static String toJsonStrSafe(Object obj) {
        return toJsonStrSafe(obj, false);
    }

    public static String toJsonStrSafe(Object obj, boolean isIgnoreNull) {
        try {
            return toJsonStr(obj, isIgnoreNull);
        } catch (Exception e) {
            log.error("Jackson convert object to string exception.", e);
            if (obj != null) {
                return obj.toString();
            } else {
                return "null";
            }
        }
    }

    public static ObjectNode createObjectNode() {
        return objectMapper.createObjectNode();
    }

    public static void writeValueToOutput(OutputStream out, Object value) throws IOException {
        objectMapper.writeValue(out, value);
    }

    public static boolean isCommonType(Object obj) {
        return (obj instanceof CharSequence) || (obj instanceof Number) || (obj instanceof Boolean);
    }
}
