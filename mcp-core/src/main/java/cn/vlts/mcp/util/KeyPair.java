package cn.vlts.mcp.util;

/**
 * 密钥对
 *
 * @author throwable
 * @version v1
 * @description 密钥对
 * @since 2023/12/29 11:55
 */
public class KeyPair {

    private final String pubKey;

    private final String priKey;

    public KeyPair(String pubKey, String priKey) {
        this.pubKey = pubKey;
        this.priKey = priKey;
    }

    public String getPubKey() {
        return pubKey;
    }

    public String getPriKey() {
        return priKey;
    }
}
