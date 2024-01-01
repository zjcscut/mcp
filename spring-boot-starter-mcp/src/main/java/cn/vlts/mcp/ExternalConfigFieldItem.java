package cn.vlts.mcp;

/**
 * 外部配置字段条目
 *
 * @author throwable
 * @version v1
 * @description 外部配置字段条目
 * @since 2023/12/21 14:56
 */
public class ExternalConfigFieldItem {

    /**
     * 字段名称
     */
    private String name;

    /**
     * 对称加解密密钥
     */
    private String key;

    /**
     * 对称加解密向量
     */
    private String iv;

    /**
     * 非对称加解密公钥
     */
    private String pubKey;

    /**
     * 非对称加解密私钥
     */
    private String priKey;

    /**
     * 内置加解密算法
     */
    private String algorithm;

    /**
     * 自定义加解密处理器全类名
     */
    private String cryptoProcessor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public String getPriKey() {
        return priKey;
    }

    public void setPriKey(String priKey) {
        this.priKey = priKey;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getCryptoProcessor() {
        return cryptoProcessor;
    }

    public void setCryptoProcessor(String cryptoProcessor) {
        this.cryptoProcessor = cryptoProcessor;
    }
}
