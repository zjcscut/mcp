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
