/*
 *
 *  * MIT License
 *  *
 *  * Copyright (c) 2024 vlts.cn
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in all
 *  * copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  * SOFTWARE.
 *
 */

package cn.vlts.example.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 * AES工具
 *
 * @author throwable
 * @version 1.0
 * @description AES工具
 * @since 2023/6/14 16:51
 **/
@Slf4j
public class AesUtils {

    public static final String AES_KEY = "1234567890123456";

    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Random RANDOM = new SecureRandom();

    private static final String ENCODING = "UTF-8";

    private static final String AES_ALGORITHM = "AES";

    /**
     * 指定填充方式
     */
    private static final String CIPHER_PADDING = "AES/ECB/PKCS5Padding";

    private static final String CIPHER_CBC_PADDING = "AES/CBC/PKCS5Padding";

    public static String encrypt(String content) {
        return encrypt(content, AES_KEY);
    }

    public static String encrypt(String content, String aesKey) {
        try {
            // 对密码进行编码
            byte[] bytes = aesKey.getBytes(ENCODING);
            // 设置加密算法 生成秘钥
            SecretKeySpec sKeySpec = new SecretKeySpec(bytes, AES_ALGORITHM);
            // 算法/模式/补码方式
            Cipher cipher = Cipher.getInstance(CIPHER_PADDING);
            // 选择加密
            cipher.init(Cipher.ENCRYPT_MODE, sKeySpec);
            // 根据待加密的内容生成字节数组
            byte[] encrypted = cipher.doFinal(content.getBytes(ENCODING));
            // 返回base64字符串
            //return Base64Utils.encodeToString(encrypted);
            byte[] encode = Base64.getEncoder().encode(encrypted);
            return new String(encode, ENCODING);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String content) {
        return decrypt(content, AES_KEY);
    }

    public static String decrypt(String content, String aesKey) {
        try {
            // 对密码进行编码
            byte[] bytes = aesKey.getBytes(ENCODING);
            // 设置解密算法，生成秘钥
            SecretKeySpec sKeySpec = new SecretKeySpec(bytes, AES_ALGORITHM);
            // 算法/补码/补码方式
            Cipher cipher = Cipher.getInstance(CIPHER_PADDING);
            // 选择解密
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec);
            // 先进行base64解码
            //   byte[] decodeBase64 = Base64Utils.decodeFromString(content);
            byte[] decodeBase64 = Base64.getDecoder().decode(content);
            // 根据待解密内容进行解密
            byte[] decrypted = cipher.doFinal(decodeBase64);
            return new String(decrypted, ENCODING);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getRandomNumber() {
        char[] nonceChars = new char[16]; //指定长度
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }
}
