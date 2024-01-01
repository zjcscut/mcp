package cn.vlts.mcp.crypto;


import cn.vlts.mcp.common.CryptoAlgorithm;
import cn.vlts.mcp.util.McpReflectionUtils;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 加解密处理器工厂默认实现
 *
 * @author throwable
 * @version v1
 * @description 加解密处理器工厂默认实现
 * @since 2023/12/20 15:35
 */
public class DefaultCryptoProcessorFactory implements CryptoProcessorFactory {

    private static final ConcurrentMap<CacheKey, DuplexStringCryptoProcessor> BUILD_IN_CACHE = new ConcurrentHashMap<>();

    private static final ConcurrentMap<CacheKey, DuplexStringCryptoProcessor> CUSTOM_CACHE = new ConcurrentHashMap<>();

    /**
     * 加载内置加解密处理器 - 不存在则进行注册
     *
     * @param algorithm algorithm
     * @param config    config
     * @return crypto processor
     */
    @Override
    public DuplexStringCryptoProcessor loadBuildInCryptoProcessor(CryptoAlgorithm algorithm, CryptoConfig config) {
        return BUILD_IN_CACHE.computeIfAbsent(new CacheKey(algorithm.getCryptoProcessor(), config), ck -> {
            DuplexStringCryptoProcessor cryptoProcessor = (DuplexStringCryptoProcessor) McpReflectionUtils.newInstance(ck.getType());
            cryptoProcessor.init(algorithm.name(), ck.getConfig());
            return cryptoProcessor;
        });
    }

    /**
     * 加载自定义加解密处理器 - 不存在则进行注册
     *
     * @param type   type
     * @param config config
     * @return crypto processor
     */
    @Override
    public DuplexStringCryptoProcessor loadCustomCryptoProcessor(Class<? extends DuplexStringCryptoProcessor> type, CryptoConfig config) {
        return CUSTOM_CACHE.computeIfAbsent(new CacheKey(type, config), ck -> {
            DuplexStringCryptoProcessor cryptoProcessor = (DuplexStringCryptoProcessor) McpReflectionUtils.newInstance(ck.getType());
            cryptoProcessor.init(ck.getType().getName(), ck.getConfig());
            return cryptoProcessor;
        });
    }

    private static class CacheKey {

        private final Class<? extends DuplexStringCryptoProcessor> type;

        private final CryptoConfig config;

        public CacheKey(Class<? extends DuplexStringCryptoProcessor> type, CryptoConfig config) {
            this.type = type;
            this.config = config;
        }

        public Class<?> getType() {
            return type;
        }

        public CryptoConfig getConfig() {
            return config;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CacheKey cacheKey = (CacheKey) o;
            return Objects.equals(type, cacheKey.type) && Objects.equals(config, cacheKey.config);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, config);
        }
    }
}
