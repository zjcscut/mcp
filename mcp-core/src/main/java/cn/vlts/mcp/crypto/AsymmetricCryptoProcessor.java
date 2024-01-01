package cn.vlts.mcp.crypto;


import cn.vlts.mcp.common.CryptoAlgorithm;
import cn.vlts.mcp.exception.McpException;
import cn.vlts.mcp.util.McpCryptoUtils;

import java.util.Optional;

/**
 * 非对称加解密处理器
 *
 * @author throwable
 * @version v1
 * @description 非对称加解密处理器
 * @since 2023/12/29 11:43
 */
public class AsymmetricCryptoProcessor implements DuplexCryptoProcessor {

    private String pubKey;

    private String priKey;

    private String pattern;

    private String algorithm;

    @Override
    public void init(String alg, CryptoConfig config) {
        pubKey = config.getPubKey();
        priKey = config.getPriKey();
        CryptoAlgorithm cryptoAlgorithm = Optional.ofNullable(CryptoAlgorithm.fromCryptoAlgorithm(alg))
                .orElseThrow(() -> new McpException(String.format("加载内置加解密算法异常,algorithm:%s", alg)));
        pattern = cryptoAlgorithm.getPattern();
        algorithm = cryptoAlgorithm.getAlgorithm();
    }

    @Override
    public byte[] processEncryption(byte[] content) {
        return McpCryptoUtils.X.doAsymmetricEncryption(algorithm, pattern, pubKey, content);
    }

    @Override
    public byte[] processDecryption(byte[] content) {
        return McpCryptoUtils.X.doAsymmetricDecryption(algorithm, pattern, priKey, content);
    }
}
