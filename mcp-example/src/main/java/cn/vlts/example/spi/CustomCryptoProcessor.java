package cn.vlts.example.spi;


import cn.vlts.mcp.codec.CodecProcessor;
import cn.vlts.mcp.common.CryptoAlgorithm;
import cn.vlts.mcp.crypto.CryptoConfig;
import cn.vlts.mcp.crypto.DuplexCryptoProcessor;
import cn.vlts.mcp.crypto.DuplexStringCryptoProcessor;
import cn.vlts.mcp.crypto.SymmetricCryptoProcessor;

import java.nio.charset.StandardCharsets;

/**
 * 自定义加解密处理器
 *
 * @author throwable
 * @version v1
 * @description 自定义加解密处理器
 * @since 2023/12/21 17:18
 */
public class CustomCryptoProcessor implements DuplexStringCryptoProcessor {

    private CodecProcessor codecProcessor;

    private DuplexCryptoProcessor duplexCryptoProcessor;

    @Override
    public void init(String alg, CryptoConfig config) {
        CryptoAlgorithm cryptoAlgorithm = CryptoAlgorithm.AES_ECB_PKCS5PADDING_BASE64;
        duplexCryptoProcessor = new SymmetricCryptoProcessor();
        duplexCryptoProcessor.init(cryptoAlgorithm.name(), config);
        codecProcessor = cryptoAlgorithm.getCodecAlgorithm().getCodecProcessor();
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
