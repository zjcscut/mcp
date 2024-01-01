package cn.vlts.mcp.crypto;

import cn.vlts.mcp.common.CryptoAlgorithm;
import cn.vlts.mcp.exception.McpException;
import cn.vlts.mcp.util.McpCryptoUtils;

import java.util.Optional;

/**
 * 对称加解密处理器
 *
 * @author throwable
 * @version v1
 * @description 对称加解密处理器
 * @since 2023/12/19 23:01
 */
public class SymmetricCryptoProcessor implements DuplexCryptoProcessor {

    private String key;

    private String iv;

    private String pattern;

    private String algorithm;

    @Override
    public void init(String alg, CryptoConfig config) {
        key = config.getKey();
        iv = config.getIv();
        CryptoAlgorithm cryptoAlgorithm = Optional.ofNullable(CryptoAlgorithm.fromCryptoAlgorithm(alg))
                .orElseThrow(() -> new McpException(String.format("加载内置加解密算法异常,algorithm:%s", alg)));
        pattern = cryptoAlgorithm.getPattern();
        algorithm = cryptoAlgorithm.getAlgorithm();
    }

    @Override
    public byte[] processDecryption(byte[] content) {
        return McpCryptoUtils.X.doSymmetricDecryption(algorithm, pattern, key, content, iv);
    }

    @Override
    public byte[] processEncryption(byte[] content) {
        return McpCryptoUtils.X.doSymmetricEncryption(algorithm, pattern, key, content, iv);
    }
}
