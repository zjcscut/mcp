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

package cn.vlts.mcp.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 加解密处理工具
 *
 * @author throwable
 * @version v1
 * @description 加解密处理工具
 * @since 2023/7/4 10:31
 */
public enum McpCryptoUtils {

    /**
     * 单例
     */
    X;

    public byte[] doSymmetricEncryption(String algorithm, String pattern, String key, byte[] data) {
        return doSymmetricEncryption(algorithm, pattern, key, data, null);
    }

    /**
     * 对称加密处理
     *
     * @param algorithm 算法
     * @param pattern   模式
     * @param key       密钥
     * @param data      明文数据体字节数组
     * @param iv        加密向量
     * @return 加密结果
     */
    public byte[] doSymmetricEncryption(String algorithm, String pattern, String key, byte[] data, String iv) {
        try {
            Cipher cipher = Cipher.getInstance(pattern);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
            if (McpStringUtils.X.isNotBlank(iv)) {
                IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(UTF_8));
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            }
            return cipher.doFinal(data);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) e;
            } else {
                throw new IllegalArgumentException(e);
            }
        }
    }

    /**
     * 非对称加密处理
     *
     * @param algorithm     算法
     * @param pattern       模式
     * @param publicKeyText 公钥
     * @param data          明文数据体字节数组
     * @return 加密结果
     */
    public byte[] doAsymmetricEncryption(String algorithm, String pattern, String publicKeyText, byte[] data) {
        try {
            PublicKey publicKey = getPublicKey(algorithm, publicKeyText);
            Cipher cipher = Cipher.getInstance(pattern);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) e;
            } else {
                throw new IllegalArgumentException(e);
            }
        }
    }

    public byte[] doSymmetricDecryption(String algorithm, String pattern, String key, byte[] data) {
        return doSymmetricDecryption(algorithm, pattern, key, data, null);
    }

    /**
     * 对称解密处理
     *
     * @param algorithm 算法
     * @param pattern   模式
     * @param key       密钥
     * @param data      加密数据体字节数组
     * @param iv        加密向量
     * @return 明文结果
     */
    public byte[] doSymmetricDecryption(String algorithm, String pattern, String key, byte[] data, String iv) {
        try {
            Cipher cipher = Cipher.getInstance(pattern);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
            if (McpStringUtils.X.isNotBlank(iv)) {
                IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(UTF_8));
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            }
            return cipher.doFinal(data);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) e;
            } else {
                throw new IllegalArgumentException(e);
            }
        }
    }

    /**
     * 非对称解密处理
     *
     * @param algorithm      算法
     * @param pattern        模式
     * @param privateKeyText 私钥
     * @param data           加密数据体字节数组
     * @return 明文结果
     */
    public byte[] doAsymmetricDecryption(String algorithm, String pattern, String privateKeyText, byte[] data) {
        try {
            PrivateKey privateKey = getPrivateKey(algorithm, privateKeyText);
            Cipher cipher = Cipher.getInstance(pattern);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) e;
            } else {
                throw new IllegalArgumentException(e);
            }
        }
    }

    private static PublicKey getPublicKey(String alg, String key) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(key.getBytes(StandardCharsets.UTF_8));
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(alg);
        return keyFactory.generatePublic(keySpec);
    }

    private static PrivateKey getPrivateKey(String alg, String key) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(key.getBytes(StandardCharsets.UTF_8));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(alg);
        return keyFactory.generatePrivate(keySpec);
    }

    public cn.vlts.mcp.util.KeyPair genKeyPair(String alg, int keySize) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(alg);
        keyPairGen.initialize(keySize, new SecureRandom());
        java.security.KeyPair keyPair = keyPairGen.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        String publicKeyText = Base64.encodeBase64String(publicKey.getEncoded());
        String privateKeyText = Base64.encodeBase64String(privateKey.getEncoded());
        return new cn.vlts.mcp.util.KeyPair(publicKeyText, privateKeyText);
    }
}
