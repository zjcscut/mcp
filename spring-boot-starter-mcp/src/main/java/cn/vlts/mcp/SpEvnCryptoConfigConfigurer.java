package cn.vlts.mcp;


import cn.vlts.mcp.crypto.CryptoConfig;
import cn.vlts.mcp.crypto.CryptoConfigConfigurer;
import cn.vlts.mcp.spi.CryptoField;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
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
    public void apply(Field field, CryptoField cryptoField, CryptoConfig config) {
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
