package cn.vlts.example.util;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 * @Author yangshijie
 * @Version 1.0
 * @Date 2023/6/14 16:51
 * @Description
 **/

@Slf4j
@Component
public class AesUtils {

    public static final String AES_KEY = "1234567890123456";

    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM = new SecureRandom();


    private static final Logger LOGGER = LoggerFactory.getLogger(AesUtils.class);

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
        if (!StringUtils.hasText(content)) {
            LOGGER.warn("需要加密的内容为空");
            return null;
        }
        //判断密钥是不是16位的
        if (StringUtils.hasText(aesKey) && aesKey.length() == 16) {
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
                LOGGER.info("AES encrypt exception:" + e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            LOGGER.info("AES encrypt: the aesKey is null or error!");
            return null;
        }
    }

    public static String decrypt(String content) {
        return decrypt(content, AES_KEY);
    }

    public static String decrypt(String content, String aesKey) {

        if (!StringUtils.hasText(content)) {
            LOGGER.info("AES decrypt: the content is null!");
            return null;
        }
        // 判断秘钥是否为16位
        if (StringUtils.hasText(aesKey) && aesKey.length() == 16) {
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
                LOGGER.info("AES decrypt exception: ", e);
                throw new RuntimeException(e);
            }
        } else {
            LOGGER.info("AES decrypt: the aesKey is null or error!");
            return null;
        }
    }


    public static String encryptCbc(String content, String aesKey, String aesIv) {

        if (!StringUtils.hasText(content)) {
            LOGGER.info("AES_CBC encrypt: the content is null!");
            return null;
        }
        // 判断秘钥是否为16位
        if (StringUtils.hasText(aesKey) && aesKey.length() == 16) {
            try {
                // 先对秘钥进行编码
                byte[] bytes = aesKey.getBytes(ENCODING);
                // 设置加密算法，并生成秘钥
                SecretKeySpec secretKeySpec = new SecretKeySpec(bytes, AES_ALGORITHM);
                // 算法、模式、补码方式
                Cipher cipher = Cipher.getInstance(CIPHER_CBC_PADDING);
                // 偏移
                IvParameterSpec ivParameterSpec = new IvParameterSpec(aesIv.getBytes(ENCODING));
                // 选择加密
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
                // 根据待加密内容 生成字节数组
                byte[] encrypted = cipher.doFinal(content.getBytes(ENCODING));
                return Base64.getEncoder().encodeToString(encrypted);
            } catch (Exception e) {
                LOGGER.info("AES_CBC encrypt exception: ", e);
                throw new RuntimeException(e);
            }
        } else {
            LOGGER.info("AES_CBC encrypt: the aesKey is null or error!");
            return null;
        }
    }

    public static String decryptCbc(String content, String aesKey, String aesIv) {

        if (!StringUtils.hasText(content)) {
            LOGGER.info("AES_CBC decrypt: the content is null!");
            return null;
        }
        // 判断密钥是不是16位
        if (StringUtils.hasText(aesKey) && aesKey.length() == 16) {
            try {
                // 编码输入的密码
                byte[] bytes = aesKey.getBytes(ENCODING);
                // 拿一个密钥工具 （AES）
                SecretKeySpec secretKeySpec = new SecretKeySpec(bytes, AES_ALGORITHM);
                // 编码、 补码 、 补码方式
                Cipher cipher = Cipher.getInstance(CIPHER_CBC_PADDING);
                // 偏移
                IvParameterSpec ivParameterSpec = new IvParameterSpec(aesIv.getBytes(ENCODING));
                // 初始化加密
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
                // 真正执行 先对内容进行base解密
                byte[] result = content.getBytes(ENCODING);
                byte[] decode = Base64.getDecoder().decode(result);
                // 进行 aes解密
                byte[] temp = cipher.doFinal(decode);
                return new String(temp, ENCODING);
            } catch (Exception e) {
                LOGGER.info("AES_CBC decrypt exception:", e);
                throw new RuntimeException(e);
            }
        } else {
            LOGGER.info("AES_CBC decrypt: the aesKey is null or error!");
            return null;
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
