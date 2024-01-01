package cn.vlts.mcp.crypto;


import cn.vlts.mcp.spi.CryptoField;
import cn.vlts.mcp.util.MultiKey;

import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * 引用类型字段加解密处理器注册中心
 *
 * @author throwable
 * @version v1
 * @description 引用类型字段加解密处理器注册中心
 * @since 2023/12/21 9:56
 */
public interface FieldCryptoProcessorRegistry {

    /**
     * 是否存在字段加解密处理器
     *
     * @param field field
     * @return exist
     */
    boolean existFieldCryptoProcessor(Field field);

    /**
     * 注册字段加解密处理器
     *
     * @param field           field
     * @param cryptoProcessor cryptoProcessor
     * @return crypto processor
     */
    <T extends DuplexStringCryptoProcessor> T registerFieldCryptoProcessor(Field field,
                                                                           DuplexStringCryptoProcessor cryptoProcessor);

    /**
     * 注册字段加解密处理器
     *
     * @param field                   field
     * @param cryptoProcessorFunction cryptoProcessorFunction
     * @return crypto processor
     */
    <T extends DuplexStringCryptoProcessor> T registerFieldCryptoProcessor(Field field,
                                                                           Function<Field, DuplexStringCryptoProcessor> cryptoProcessorFunction);

    /**
     * 获取字段加解密处理器
     *
     * @param field field
     * @return crypto processor
     */
    <T extends DuplexStringCryptoProcessor> T getFieldCryptoProcessor(Field field);

    /**
     * 是否存在字段注解加解密处理器
     *
     * @param cryptoField cryptoField
     * @return exist
     */
    boolean existFieldAnnotationCryptoProcessor(CryptoField cryptoField);

    /**
     * 注册字段注解加解密处理器
     *
     * @param cryptoField     cryptoField
     * @param cryptoProcessor cryptoProcessor
     * @return crypto processor
     */
    <T extends DuplexStringCryptoProcessor> T registerFieldAnnotationCryptoProcessor(CryptoField cryptoField,
                                                                                     DuplexStringCryptoProcessor cryptoProcessor);

    /**
     * 注册字段注解加解密处理器
     *
     * @param cryptoField             cryptoField
     * @param cryptoProcessorFunction cryptoProcessorFunction
     * @return crypto processor
     */
    <T extends DuplexStringCryptoProcessor> T registerFieldAnnotationCryptoProcessor(CryptoField cryptoField,
                                                                                     Function<CryptoField, DuplexStringCryptoProcessor> cryptoProcessorFunction);

    /**
     * 获取字段注解加解密处理器
     *
     * @param cryptoField cryptoField
     * @return crypto processor
     */
    <T extends DuplexStringCryptoProcessor> T getFieldAnnotationCryptoProcessor(CryptoField cryptoField);

    /**
     * 注册混合KEY加解密处理器
     *
     * @param multiKey        multiKey
     * @param cryptoProcessor cryptoProcessor
     * @return crypto processor
     */
    <T extends DuplexStringCryptoProcessor> T registerKeysCryptoProcessor(MultiKey multiKey,
                                                                          DuplexStringCryptoProcessor cryptoProcessor);

    /**
     * 注册混合KEY加解密处理器
     *
     * @param multiKey                multiKey
     * @param cryptoProcessorFunction cryptoProcessorFunction
     * @return crypto processor
     */
    <T extends DuplexStringCryptoProcessor> T registerKeysCryptoProcessor(MultiKey multiKey,
                                                                          Function<MultiKey, DuplexStringCryptoProcessor> cryptoProcessorFunction);

    /**
     * 获取混合KEY加解密处理器
     *
     * @param multiKey multiKey
     * @return crypto processor
     */
    <T extends DuplexStringCryptoProcessor> T getKeysCryptoProcessor(MultiKey multiKey);
}
