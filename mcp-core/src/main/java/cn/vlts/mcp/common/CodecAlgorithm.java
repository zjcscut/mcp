package cn.vlts.mcp.common;


import cn.vlts.mcp.codec.Base64CodecProcessor;
import cn.vlts.mcp.codec.CodecProcessor;
import cn.vlts.mcp.codec.HexCodecProcessor;
import cn.vlts.mcp.codec.RawCodecProcessor;

/**
 * 编码解码算法
 *
 * @author throwable
 * @version v1
 * @description 编码解码算法
 * @since 2023/12/19 15:48
 */
public enum CodecAlgorithm {

    /**
     * 原始
     */
    RAW(RawCodecProcessor.INSTANCE),

    /**
     * 十六进制
     */
    HEX(HexCodecProcessor.INSTANCE),

    /**
     * BASE64
     */
    BASE64(Base64CodecProcessor.INSTANCE),

    ;

    private final CodecProcessor codecProcessor;

    CodecAlgorithm(CodecProcessor codecProcessor) {
        this.codecProcessor = codecProcessor;
    }

    public CodecProcessor getCodecProcessor() {
        return codecProcessor;
    }
}
