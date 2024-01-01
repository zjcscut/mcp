package cn.vlts.mcp.crypto;

/**
 * 字符串解密处理器
 *
 * @author throwable
 * @version v1
 * @description 字符串解密处理器
 * @since 2023/12/19 15:58
 */
@FunctionalInterface
public interface StringDecryptProcessor {

    /**
     * 解密前回调
     *
     * @param content content
     * @return result
     */
    default String processBeforeDecryption(String content) {
        return content;
    }

    /**
     * 处理解密
     *
     * @param content 密文内容
     * @return 明文内容
     */
    String processDecryption(String content);

    /**
     * 解密后回调
     *
     * @param content content
     * @return result
     */
    default String processAfterDecryption(String content) {
        return content;
    }
}
