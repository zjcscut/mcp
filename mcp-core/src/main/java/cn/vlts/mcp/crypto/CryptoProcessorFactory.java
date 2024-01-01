package cn.vlts.mcp.crypto;


import cn.vlts.mcp.common.CryptoAlgorithm;

/**
 * 加解密处理器工厂
 *
 * @author throwable
 * @version v1
 * @description 加解密处理器工厂
 * @since 2023/12/21 9:52
 */
public interface CryptoProcessorFactory {

    /**
     * 加载内置加解密处理器 - 不存在则进行创建
     *
     * @param algorithm algorithm
     * @param config    config
     * @return internal crypto processor
     */
    DuplexStringCryptoProcessor loadBuildInCryptoProcessor(CryptoAlgorithm algorithm,
                                                           CryptoConfig config);

    /**
     * 加载自定义加解密处理器 - 不存在则进行创建
     *
     * @param type   type
     * @param config config
     * @return custom crypto processor
     */
    DuplexStringCryptoProcessor loadCustomCryptoProcessor(Class<? extends DuplexStringCryptoProcessor> type,
                                                          CryptoConfig config);
}
