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


import cn.vlts.mcp.crypto.CryptoConfig;
import cn.vlts.mcp.crypto.CryptoConfigConfigurer;
import cn.vlts.mcp.spi.CryptoTarget;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * Spring Environment加解密配置器
 *
 * @author throwable
 * @version v1
 * @description Spring Environment加解密配置器
 * @since 2023/12/21 14:15
 */
public class SpEvnCryptoConfigConfigurer implements CryptoConfigConfigurer {

    private static final String PLACEHOLDER_PREFIX = "${";

    private final DefaultListableBeanFactory beanFactory;

    public SpEvnCryptoConfigConfigurer(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void apply(CryptoTarget cryptoTarget, CryptoConfig config) {
        config.setKey(evaluateValue(config.getKey()));
        config.setIv(evaluateValue(config.getIv()));
        config.setPubKey(evaluateValue(config.getPubKey()));
        config.setPriKey(evaluateValue(config.getPriKey()));
    }

    /**
     * 计算值，如果值以${开头则从Spring Environment中重新计算值覆盖原始值
     *
     * @param value value
     * @return result
     */
    private String evaluateValue(String value) {
        if (!StringUtils.hasLength(value) || !StringUtils.startsWithIgnoreCase(value, PLACEHOLDER_PREFIX)) {
            return value;
        }
        String targetValue = beanFactory.resolveEmbeddedValue(value);
        BeanExpressionResolver beanExpressionResolver = beanFactory.getBeanExpressionResolver();
        if (Objects.isNull(beanExpressionResolver)) {
            return targetValue;
        }
        return String.valueOf(beanExpressionResolver.evaluate(targetValue, new BeanExpressionContext(beanFactory, null)));
    }
}
