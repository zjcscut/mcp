package cn.vlts.mcp.crypto;

/**
 * 加密处理器
 *
 * @author throwable
 * @version v1
 * @description 加密处理器
 * @since 2023/12/19 15:58
 */
@FunctionalInterface
public interface EncryptProcessor extends CryptoProcessor {

    /**
     * 处理加密
     *
     * @param content 明文内容
     * @return 密文内容
     */
    byte[] processEncryption(byte[] content);
}
