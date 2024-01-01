package cn.vlts.mcp.codec;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;

/**
 * 十六进制编码解码处理器
 *
 * @author throwable
 * @version v1
 * @description 十六进制编码解码处理器
 * @since 2023/12/19 20:34
 */
public class HexCodecProcessor implements CodecProcessor {

    public static final CodecProcessor INSTANCE = new HexCodecProcessor();

    @Override
    public byte[] encode(byte[] bytes) {
        return new String(Hex.encodeHex(bytes)).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] decode(byte[] bytes) {
        String chars = new String(bytes, StandardCharsets.UTF_8);
        try {
            return Hex.decodeHex(chars);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String encodeToString(byte[] content) {
        return Hex.encodeHexString(content);
    }

    @Override
    public byte[] decodeFromString(String content) {
        try {
            return Hex.decodeHex(content);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
