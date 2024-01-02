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

package cn.vlts.mcp;

import cn.vlts.mcp.config.McpConfig;
import cn.vlts.mcp.crypto.*;
import cn.vlts.mcp.spi.*;
import cn.vlts.mcp.util.McpReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * MCP自动配置类
 *
 * @author throwable
 * @version v1
 * @description MCP自动配置类
 * @since 2023/12/21 11:07
 */
@Configuration
@EnableConfigurationProperties(McpProperties.class)
@ConditionalOnProperty(value = "mcp.enabled", havingValue = "true", matchIfMissing = true)
public class McpAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(McpAutoConfiguration.class);

    private final McpProperties mcpProperties;

    private volatile McpConfig mcpConfig;

    public McpAutoConfiguration(McpProperties mcpProperties) {
        this.mcpProperties = mcpProperties;
    }

    @Bean
    public SpEvnCryptoConfigConfigurer spEvnCryptoConfigConfigurer(DefaultListableBeanFactory beanFactory) {
        return new SpEvnCryptoConfigConfigurer(beanFactory);
    }

    @Bean(destroyMethod = "close")
    public McpInitializer mybatisCodecPluginInitializer(CryptoProcessorFactory cryptoProcessorFactory,
                                                        FieldCryptoProcessorRegistry fieldCryptoProcessorRegistry,
                                                        FieldCryptoProcessorChooser fieldCryptoProcessorChooser,
                                                        GlobalConfigProvider globalConfigProvider,
                                                        ObjectProvider<List<CryptoConfigConfigurer>> cryptoConfigConfigurerList) {
        McpInitializer initializer = new McpInitializer(
                mcpProperties.getTypePackages(),
                cryptoProcessorFactory,
                fieldCryptoProcessorRegistry,
                fieldCryptoProcessorChooser,
                globalConfigProvider,
                Optional.ofNullable(cryptoConfigConfigurerList.getIfAvailable()).orElse(new ArrayList<>())
        );
        initializer.initAsync();
        return initializer;
    }

    @Bean
    public CryptoProcessorFactory cryptoProcessorFactory() {
        McpReflectionUtils.attachExternalProvider(new SpringExternalReflectionProvider());
        return new DefaultCryptoProcessorFactory();
    }

    @Bean
    public FieldCryptoProcessorRegistry fieldCryptoProcessorRegistry() {
        return new DefaultFieldCryptoProcessorRegistry();
    }

    @Bean
    public McpInternalInterceptor mybatisCryptoInternalInterceptor(CryptoProcessorFactory cryptoProcessorFactory,
                                                                   FieldCryptoProcessorRegistry fieldCryptoProcessorRegistry,
                                                                   ObjectProvider<List<CryptoConfigConfigurer>> cryptoConfigConfigurerList,
                                                                   ObjectProvider<ReferenceCryptoMatcher> referenceCryptoMatcher) {
        return new McpInternalInterceptor(
                toMcpConfig(),
                cryptoProcessorFactory,
                fieldCryptoProcessorRegistry,
                Optional.ofNullable(cryptoConfigConfigurerList.getIfAvailable()).orElse(new ArrayList<>()),
                Optional.ofNullable(referenceCryptoMatcher.getIfAvailable()).orElse(ReferenceCryptoMatcher.DEFAULT)
        );
    }

    @Bean
    public McpCoreInterceptor mybatisCodecCoreInterceptor(ObjectProvider<List<InternalInterceptor>> internalInterceptorList) {
        McpCoreInterceptor mcpCoreInterceptor = new McpCoreInterceptor();
        Optional.ofNullable(internalInterceptorList.getIfAvailable())
                .orElse(new ArrayList<>())
                .forEach(mcpCoreInterceptor::addInnerInterceptor);
        return mcpCoreInterceptor;
    }

    @SuppressWarnings("unchecked")
    public McpConfig toMcpConfig() {
        if (Objects.isNull(mcpConfig)) {
            synchronized (McpConfig.class) {
                if (Objects.isNull(mcpConfig)) {
                    mcpConfig = new McpConfig();
                    mcpConfig.setEnabled(mcpProperties.getEnabled());
                    mcpConfig.setFastFail(mcpProperties.getFastFail());
                    mcpConfig.setMode(mcpProperties.getMode());
                    mcpConfig.setGlobalKey(mcpProperties.getGlobalKey());
                    mcpConfig.setGlobalPubKey(mcpProperties.getGlobalPubKey());
                    mcpConfig.setGlobalPriKey(mcpProperties.getGlobalPriKey());
                    mcpConfig.setGlobalCryptoAlgorithm(mcpProperties.getGlobalCryptoAlgorithm());
                    String globalCryptoProcessor = mcpProperties.getGlobalCryptoProcessor();
                    if (StringUtils.hasLength(globalCryptoProcessor)) {
                        try {
                            Class<?> clazz = ClassUtils.forName(globalCryptoProcessor, null);
                            if (ClassUtils.isAssignable(DuplexStringCryptoProcessor.class, clazz)) {
                                mcpConfig.setGlobalCryptoProcessor((Class<? extends DuplexStringCryptoProcessor>) clazz);
                            }
                        } catch (Exception e) {
                            LOGGER.warn("加载全局加解密处理器-{}失败,", globalCryptoProcessor, e);
                        }
                    }
                }
            }
        }
        return mcpConfig;
    }
}
