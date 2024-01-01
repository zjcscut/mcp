package cn.vlts.mcp.crypto;

import java.util.Objects;

/**
 * 加解密配置
 *
 * @author throwable
 * @version v1
 * @description 加解密配置
 * @since 2023/12/19 15:56
 */
public class CryptoConfig {

    /**
     * 公钥
     */
    private String pubKey;

    /**
     * 私钥
     */
    private String priKey;

    /**
     * 对称秘钥
     */
    private String key;

    /**
     * 加密向量
     */
    private String iv;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CryptoConfig that = (CryptoConfig) o;
        return Objects.equals(pubKey, that.pubKey) && Objects.equals(priKey, that.priKey) && Objects.equals(key, that.key)
                && Objects.equals(iv, that.iv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pubKey, priKey, key, iv);
    }
}
