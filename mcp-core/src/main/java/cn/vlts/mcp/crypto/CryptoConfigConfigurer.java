package cn.vlts.mcp.crypto;

import cn.vlts.mcp.spi.CryptoField;

import java.lang.reflect.Field;

/**
 * 加解密配置配置器
 *
 * @author throwable
 * @version v1
 * @description 加解密配置配置器
 * @since 2023/12/20 20:31
 */

@FunctionalInterface
public interface CryptoConfigConfigurer {

    /**
     * 匹配
     *
     * @param field       field
     * @param cryptoField cryptoField
     * @return match result
     */
    default boolean match(Field field, CryptoField cryptoField) {
        return true;
    }

    /**
     * 排序号
     *
     * @return order
     */
    default int order() {
        return Integer.MAX_VALUE;
    }

    /**
     * 应用
     *
     * @param field       field
     * @param cryptoField cryptoField
     * @param config      config
     */
    void apply(Field field, CryptoField cryptoField, CryptoConfig config);
}
