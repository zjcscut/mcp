package cn.vlts.mcp.crypto;

/**
 * 原始字符串加解密处理器 - 不做加解密处理
 *
 * @author throwable
 * @version v1
 * @description 原始字符串加解密处理器
 * @since 2023/12/20 9:59
 */
public class RawStringCryptoProcessor implements DuplexStringCryptoProcessor {

    public static final DuplexStringCryptoProcessor INSTANCE = new RawStringCryptoProcessor();

    @Override
    public String processDecryption(String content) {
        return content;
    }

    @Override
    public String processEncryption(String content) {
        return content;
    }
}
