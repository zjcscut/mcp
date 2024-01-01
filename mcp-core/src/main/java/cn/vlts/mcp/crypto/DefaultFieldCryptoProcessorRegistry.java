package cn.vlts.mcp.crypto;


import cn.vlts.mcp.spi.CryptoField;
import cn.vlts.mcp.util.MultiKey;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * 引用类型字段加解密处理器注册中心默认实现
 *
 * @author throwable
 * @version v1
 * @description 引用类型字段加解密处理器注册中心默认实现
 * @since 2023/12/21 9:58
 */
@SuppressWarnings("unchecked")
public class DefaultFieldCryptoProcessorRegistry implements FieldCryptoProcessorRegistry {

    private static final ConcurrentMap<CryptoField, DuplexStringCryptoProcessor> FIELD_ANNO_CP_CACHE = new ConcurrentHashMap<>(64);

    private static final ConcurrentMap<Field, DuplexStringCryptoProcessor> FIELD_CP_CACHE = new ConcurrentHashMap<>(64);

    private static final ConcurrentMap<MultiKey, DuplexStringCryptoProcessor> KEYS_CP_CACHE = new ConcurrentHashMap<>(64);

    @Override
    public boolean existFieldCryptoProcessor(Field field) {
        return FIELD_CP_CACHE.containsKey(field);
    }

    @Override
    public <T extends DuplexStringCryptoProcessor> T registerFieldCryptoProcessor(Field field,
                                                                                  DuplexStringCryptoProcessor cryptoProcessor) {
        FIELD_CP_CACHE.putIfAbsent(field, cryptoProcessor);
        return (T) cryptoProcessor;
    }

    @Override
    public <T extends DuplexStringCryptoProcessor> T registerFieldCryptoProcessor(Field field,
                                                                                  Function<Field, DuplexStringCryptoProcessor> cryptoProcessorFunction) {
        return (T) FIELD_CP_CACHE.computeIfAbsent(field, cryptoProcessorFunction);
    }

    @Override
    public <T extends DuplexStringCryptoProcessor> T getFieldCryptoProcessor(Field field) {
        return (T) FIELD_CP_CACHE.get(field);
    }

    @Override
    public boolean existFieldAnnotationCryptoProcessor(CryptoField cryptoField) {
        return FIELD_ANNO_CP_CACHE.containsKey(cryptoField);
    }

    @Override
    public <T extends DuplexStringCryptoProcessor> T registerFieldAnnotationCryptoProcessor(CryptoField cryptoField,
                                                                                            DuplexStringCryptoProcessor cryptoProcessor) {
        FIELD_ANNO_CP_CACHE.putIfAbsent(cryptoField, cryptoProcessor);
        return (T) cryptoProcessor;
    }

    @Override
    public <T extends DuplexStringCryptoProcessor> T registerFieldAnnotationCryptoProcessor(CryptoField cryptoField,
                                                                                            Function<CryptoField, DuplexStringCryptoProcessor> cryptoProcessorFunction) {
        return (T) FIELD_ANNO_CP_CACHE.computeIfAbsent(cryptoField, cryptoProcessorFunction);
    }

    @Override
    public <T extends DuplexStringCryptoProcessor> T getFieldAnnotationCryptoProcessor(CryptoField cryptoField) {
        return (T) FIELD_ANNO_CP_CACHE.get(cryptoField);
    }

    @Override
    public <T extends DuplexStringCryptoProcessor> T registerKeysCryptoProcessor(MultiKey multiKey,
                                                                                 DuplexStringCryptoProcessor cryptoProcessor) {
        KEYS_CP_CACHE.putIfAbsent(multiKey, cryptoProcessor);
        return (T) cryptoProcessor;
    }

    @Override
    public <T extends DuplexStringCryptoProcessor> T registerKeysCryptoProcessor(MultiKey multiKey,
                                                                                 Function<MultiKey, DuplexStringCryptoProcessor> cryptoProcessorFunction) {
        return (T) KEYS_CP_CACHE.computeIfAbsent(multiKey, cryptoProcessorFunction);
    }

    @Override
    public <T extends DuplexStringCryptoProcessor> T getKeysCryptoProcessor(MultiKey multiKey) {
        return (T) KEYS_CP_CACHE.get(multiKey);
    }
}
