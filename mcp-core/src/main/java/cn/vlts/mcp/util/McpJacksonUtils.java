package cn.vlts.mcp.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

/**
 * @author throwable
 * @version v1
 * @description Jackson工具类
 * @since 2023/12/20 9:45
 */
public enum McpJacksonUtils {

    /**
     *
     */
    X;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String format(Object target) {
        if (Objects.isNull(target)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(target);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public byte[] toByteArray(Object target) {
        if (Objects.isNull(target)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsBytes(target);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public <T> T parse(String content, Class<T> targetType) {
        if (McpStringUtils.X.isEmpty(content)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(content, targetType);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public <T> T parse(byte[] content, Class<T> targetType) {
        if (content.length == 0) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(content, targetType);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public <T> List<T> parseList(byte[] content, Class<T> targetType) {
        if (content.length == 0) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readerForListOf(targetType).readValue(content);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public <T> T parse(String content, TypeReference<T> typeReference) {
        if (McpStringUtils.X.isEmpty(content)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(content, typeReference);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public <T> T parse(byte[] content, TypeReference<T> typeReference) {
        if (content.length == 0) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(content, typeReference);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
