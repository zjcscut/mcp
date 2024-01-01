package cn.vlts.mcp.crypto;



import cn.vlts.mcp.codec.CodecProcessor;
import cn.vlts.mcp.common.CryptoAlgorithm;

import java.nio.charset.StandardCharsets;

/**
 * 基于非对称加解密算法实现的字符串加解密处理器
 *
 * @author throwable
 * @version v1
 * @description 基于非对称加解密算法实现的字符串加解密处理器
 * @since 2023/12/19 20:50
 */
public class AsymmetricStringCryptoProcessor implements DuplexStringCryptoProcessor {

    private CodecProcessor codecProcessor;

    private DuplexCryptoProcessor duplexCryptoProcessor;

    @Override
    public void init(String alg, CryptoConfig config) {
        duplexCryptoProcessor = new AsymmetricCryptoProcessor();
        duplexCryptoProcessor.init(alg, config);
        codecProcessor = CryptoAlgorithm.fromValidCryptoAlgorithm(alg).getCodecAlgorithm().getCodecProcessor();
    }

    @Override
    public String processDecryption(String content) {
        byte[] bytes = codecProcessor.decodeFromString(content);
        byte[] result = duplexCryptoProcessor.processDecryption(bytes);
        return new String(result, StandardCharsets.UTF_8);
    }

    @Override
    public String processEncryption(String content) {
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        byte[] result = duplexCryptoProcessor.processEncryption(bytes);
        return codecProcessor.encodeToString(result);
    }
}
