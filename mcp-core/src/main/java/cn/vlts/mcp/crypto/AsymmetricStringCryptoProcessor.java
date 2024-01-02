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

package cn.vlts.mcp.crypto;



import cn.vlts.mcp.codec.CodecProcessor;
import cn.vlts.mcp.common.CryptoAlgorithm;

import java.nio.charset.StandardCharsets;

/**
 * 基于非对称加解密算法实现的字符串加解密处理器
 *
 * @author throwable
 * @version v1
 * @description 基于非对称加解密算法实现的字符串加解密处理器
 * @since 2023/12/19 20:50
 */
public class AsymmetricStringCryptoProcessor implements DuplexStringCryptoProcessor {

    private CodecProcessor codecProcessor;

    private DuplexCryptoProcessor duplexCryptoProcessor;

    @Override
    public void init(String alg, CryptoConfig config) {
        duplexCryptoProcessor = new AsymmetricCryptoProcessor();
        duplexCryptoProcessor.init(alg, config);
        codecProcessor = CryptoAlgorithm.fromValidCryptoAlgorithm(alg).getCodecAlgorithm().getCodecProcessor();
    }

    @Override
    public String processDecryption(String content) {
        byte[] bytes = codecProcessor.decodeFromString(content);
        byte[] result = duplexCryptoProcessor.processDecryption(bytes);
        return new String(result, StandardCharsets.UTF_8);
    }

    @Override
    public String processEncryption(String content) {
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        byte[] result = duplexCryptoProcessor.processEncryption(bytes);
        return codecProcessor.encodeToString(result);
    }
}
