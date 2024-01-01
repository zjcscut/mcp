package cn.vlts.mcp.spi;


import cn.vlts.mcp.crypto.DuplexStringCryptoProcessor;

/**
 * 全局配置提供者
 *
 * @author throwable
 * @version v1
 * @description 全局配置提供者
 * @since 2023/12/21 15:41
 */
public interface GlobalConfigProvider {

    /**
     * 获取全局加解密密钥
     *
     * @return global key
     */
    String getGlobalKey();

    /**
     * 获取全局加解密向量
     *
     * @return global iv
     */
    String getGlobalIv();

    /**
     * 获取全局加解密公钥
     *
     * @return global public key
     */
    String getGlobalPubKey();

    /**
     * 获取全局加解密私钥
     *
     * @return global private key
     */
    String getGlobalPriKey();

    /**
     * 获取全局加解密处理器
     *
     * @return global crypto processor
     */
    DuplexStringCryptoProcessor getGlobalCryptoProcessor();
}
