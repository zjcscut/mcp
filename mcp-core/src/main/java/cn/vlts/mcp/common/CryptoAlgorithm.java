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

package cn.vlts.mcp.common;


import cn.vlts.mcp.crypto.AsymmetricStringCryptoProcessor;
import cn.vlts.mcp.crypto.DuplexStringCryptoProcessor;
import cn.vlts.mcp.crypto.RawStringCryptoProcessor;
import cn.vlts.mcp.crypto.SymmetricStringCryptoProcessor;

import java.util.Objects;

/**
 * 加解密算法
 *
 * @author throwable
 * @version v1
 * @description 加解密算法
 * @since 2023/12/19 15:57
 */
public enum CryptoAlgorithm {

    RAW("RAW", "RAW", RawStringCryptoProcessor.class, CodecAlgorithm.RAW),

    AES_ECB_PKCS5PADDING_HEX("AES", "AES/ECB/PKCS5Padding", SymmetricStringCryptoProcessor.class, CodecAlgorithm.HEX),

    AES_ECB_PKCS5PADDING_BASE64("AES", "AES/ECB/PKCS5Padding", SymmetricStringCryptoProcessor.class, CodecAlgorithm.BASE64),

    AES_CBC_PKCS5PADDING_HEX("AES", "AES/CBC/PKCS5Padding", SymmetricStringCryptoProcessor.class, CodecAlgorithm.HEX),

    AES_CBC_PKCS5PADDING_BASE64("AES", "AES/CBC/PKCS5Padding", SymmetricStringCryptoProcessor.class, CodecAlgorithm.BASE64),

    DES_ECB_PKCS5PADDING_HEX("DES", "DES/ECB/PKCS5Padding", SymmetricStringCryptoProcessor.class, CodecAlgorithm.HEX),

    DES_ECB_PKCS5PADDING_BASE64("DES", "DES/ECB/PKCS5Padding", SymmetricStringCryptoProcessor.class, CodecAlgorithm.BASE64),

    DES_CBC_PKCS5PADDING_HEX("DES", "DES/CBC/PKCS5Padding", SymmetricStringCryptoProcessor.class, CodecAlgorithm.HEX),

    DEC_CBC_PKCS5PADDING_BASE64("DES", "DES/CBC/PKCS5Padding", SymmetricStringCryptoProcessor.class, CodecAlgorithm.BASE64),

    DESEDE_ECB_PKCS5PADDING_HEX("DESede", "DESede/ECB/PKCS5Padding", SymmetricStringCryptoProcessor.class, CodecAlgorithm.HEX),

    DESEDE_ECB_PKCS5PADDING_BASE64("DESede", "DESede/ECB/PKCS5Padding", SymmetricStringCryptoProcessor.class, CodecAlgorithm.BASE64),

    DESEDE_CBC_PKCS5PADDING_HEX("DESede", "DESede/CBC/PKCS5Padding", SymmetricStringCryptoProcessor.class, CodecAlgorithm.HEX),

    DESEDE_CBC_PKCS5PADDING_BASE64("DESede", "DESede/CBC/PKCS5Padding", SymmetricStringCryptoProcessor.class, CodecAlgorithm.BASE64),

    RSA_HEX("RSA", "RSA", AsymmetricStringCryptoProcessor.class, CodecAlgorithm.HEX),

    RSA_BASE64("RSA", "RSA", AsymmetricStringCryptoProcessor.class, CodecAlgorithm.BASE64),

    RSA_ECB_PKCS1PADDING_HEX("RSA", "RSA/ECB/PKCS1Padding", AsymmetricStringCryptoProcessor.class, CodecAlgorithm.HEX),

    RSA_ECB_PKCS1PADDING_BASE64("RSA", "RSA/ECB/PKCS1Padding", AsymmetricStringCryptoProcessor.class, CodecAlgorithm.BASE64),

    ;

    private final String algorithm;

    private final String pattern;

    private final Class<? extends DuplexStringCryptoProcessor> cryptoProcessor;

    private final CodecAlgorithm codecAlgorithm;

    CryptoAlgorithm(String algorithm,
                    String pattern,
                    Class<? extends DuplexStringCryptoProcessor> cryptoProcessor,
                    CodecAlgorithm codecAlgorithm) {
        this.algorithm = algorithm;
        this.pattern = pattern;
        this.cryptoProcessor = cryptoProcessor;
        this.codecAlgorithm = codecAlgorithm;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getPattern() {
        return pattern;
    }

    public Class<? extends DuplexStringCryptoProcessor> getCryptoProcessor() {
        return cryptoProcessor;
    }

    public CodecAlgorithm getCodecAlgorithm() {
        return codecAlgorithm;
    }

    public static CryptoAlgorithm fromCryptoAlgorithm(String otherCryptoAlgorithm) {
        for (CryptoAlgorithm cryptoAlgorithm : CryptoAlgorithm.values()) {
            if (Objects.equals(cryptoAlgorithm.name(), otherCryptoAlgorithm)) {
                return cryptoAlgorithm;
            }
        }
        return null;
    }

    public static CryptoAlgorithm fromValidCryptoAlgorithm(String otherCryptoAlgorithm) {
        for (CryptoAlgorithm cryptoAlgorithm : CryptoAlgorithm.values()) {
            if (Objects.equals(cryptoAlgorithm.name(), otherCryptoAlgorithm)) {
                return cryptoAlgorithm;
            }
        }
        throw new IllegalArgumentException("CryptoAlgorithm = " + otherCryptoAlgorithm);
    }
}
