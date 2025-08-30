package cn.easygd.dynaguard.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * json工具类
 *
 * @author VD
 */
public class JsonUtils {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * objectMapper
     */
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
    }

    /**
     * json转JsonNode
     *
     * @param json json
     * @return JsonNode
     */
    public static JsonNode parse(String json) {
        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (Exception e) {
            log.warn("json parse error: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * json转对象
     *
     * @param json json
     * @param type 对象类型
     * @param <T>  对象类型
     * @return 对象
     */
    public static <T> T parse(String json, TypeReference<T> type) {
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (Exception e) {
            log.warn("json parse error: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * json转对象
     *
     * @param json  json
     * @param clazz 对象类型
     * @param <T>   对象类型
     * @return 对象
     */
    public static <T> T parse(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            log.warn("json parse error: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 对象转json
     *
     * @param obj 对象
     * @return json
     */
    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("obj to json error: {}", e.getMessage(), e);
            return null;
        }
    }
}
