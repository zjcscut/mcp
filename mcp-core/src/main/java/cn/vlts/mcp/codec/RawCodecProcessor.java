package cn.vlts.mcp.codec;

/**
 * 原始编码解码处理器
 *
 * @author throwable
 * @version v1
 * @description 原始编码解码处理器
 * @since 2023/12/19 15:53
 */
public class RawCodecProcessor implements CodecProcessor {

    public static final CodecProcessor INSTANCE = new RawCodecProcessor();

    @Override
    public byte[] encode(byte[] bytes) {
        return bytes;
    }

    @Override
    public byte[] decode(byte[] bytes) {
        return bytes;
    }
}
