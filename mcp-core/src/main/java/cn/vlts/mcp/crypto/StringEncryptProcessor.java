package cn.vlts.mcp.crypto;

/**
 * 字符串加密处理器
 *
 * @author throwable
 * @version v1
 * @description 字符串加密处理器
 * @since 2023/12/19 15:58
 */
@FunctionalInterface
public interface StringEncryptProcessor {

    /**
     * 加密前回调
     *
     * @param content content
     * @return result
     */
    default String processBeforeEncryption(String content) {
        return content;
    }

    /**
     * 处理加密
     *
     * @param content 明文内容
     * @return 密文内容
     */
    String processEncryption(String content);

    /**
     * 加密后回调
     *
     * @param content content
     * @return result
     */
    default String processAfterEncryption(String content) {
        return content;
    }
}
