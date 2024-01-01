package cn.vlts.mcp.spi;


import cn.vlts.mcp.common.CryptoAlgorithm;
import cn.vlts.mcp.crypto.DuplexStringCryptoProcessor;

import java.lang.annotation.*;

/**
 * 支持加解密的字段注解
 *
 * @author throwable
 * @version v1
 * @description 支持加解密的字段注解
 * @since 2023/12/19 15:38
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CryptoField {

    /**
     * 对称加解密KEY，支持从Spring Environment获取
     *
     * @return key
     */
    String key() default "";

    /**
     * 对称加解密IV，支持从Spring Environment获取
     *
     * @return iv
     */
    String iv() default "";

    /**
     * 非对称加密Public Key，支持从Spring Environment获取
     *
     * @return public key
     */
    String pubKey() default "";

    /**
     * 非对称加密Private Key，支持从Spring Environment获取
     *
     * @return private key
     */
    String priKey() default "";

    /**
     * 内置加解密算法
     */
    CryptoAlgorithm algorithm() default CryptoAlgorithm.RAW;

    /**
     * 字符串加解密处理器
     */
    Class<? extends DuplexStringCryptoProcessor> cryptoProcessor() default DuplexStringCryptoProcessor.class;
}
