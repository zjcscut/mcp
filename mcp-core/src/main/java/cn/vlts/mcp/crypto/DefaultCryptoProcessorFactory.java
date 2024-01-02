/*
 * MIT License
 *
 * Copyright (c) 2024 vlts.cn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
