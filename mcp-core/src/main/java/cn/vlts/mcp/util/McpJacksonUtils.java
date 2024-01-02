/*
 * MIT License
 *
 * Copyright (c) 2024 vlts.cn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
