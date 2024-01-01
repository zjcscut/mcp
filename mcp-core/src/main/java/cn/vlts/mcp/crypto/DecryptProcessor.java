package cn.vlts.mcp.crypto;

/**
 * 解密处理器
 *
 * @author throwable
 * @version v1
 * @description 解密处理器
 * @since 2023/12/19 15:58
 */
@FunctionalInterface
public interface DecryptProcessor extends CryptoProcessor {

    /**
     * 处理解密
     *
     * @param content 密文内容
     * @return 明文内容
     */
    byte[] processDecryption(byte[] content);
}
