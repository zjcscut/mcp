package cn.vlts.mcp.codec;

import java.nio.charset.StandardCharsets;

/**
 * 编码解码处理器
 *
 * @author throwable
 * @version v1
 * @description 编码解码处理器
 * @since 2023/12/19 15:49
 */
public interface CodecProcessor {

    /**
     * 编码
     *
     * @param bytes bytes
     * @return result
     */
    byte[] encode(byte[] bytes);

    /**
     * 解码
     *
     * @param bytes bytes
     * @return result
     */
    byte[] decode(byte[] bytes);

    default String encodeToString(byte[] content) {
        return new String(this.encode(content), StandardCharsets.UTF_8);
    }

    default String encodeString(String content) {
        return new String(this.encode(content.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    default String decodeString(String content) {
        return new String(this.decode(content.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    default byte[] decodeFromString(String content) {
        return this.decode(content.getBytes(StandardCharsets.UTF_8));
    }
}
