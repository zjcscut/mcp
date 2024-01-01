package cn.vlts.mcp.codec;

import org.apache.commons.codec.binary.Base64;

/**
 * Base64编码解码处理器
 *
 * @author throwable
 * @version v1
 * @description Base64编码解码处理器
 * @since 2023/12/19 20:40
 */
public class Base64CodecProcessor implements CodecProcessor {

    public static final CodecProcessor INSTANCE = new Base64CodecProcessor();

    @Override
    public byte[] encode(byte[] bytes) {
        return Base64.encodeBase64(bytes);
    }

    @Override
    public byte[] decode(byte[] bytes) {
        return Base64.decodeBase64(bytes);
    }
}
