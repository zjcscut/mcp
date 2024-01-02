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

import cn.vlts.mcp.common.CryptoAlgorithm;
import cn.vlts.mcp.exception.McpException;
import cn.vlts.mcp.util.McpCryptoUtils;

import java.util.Optional;

/**
 * 对称加解密处理器
 *
 * @author throwable
 * @version v1
 * @description 对称加解密处理器
 * @since 2023/12/19 23:01
 */
public class SymmetricCryptoProcessor implements DuplexCryptoProcessor {

    private String key;

    private String iv;

    private String pattern;

    private String algorithm;

    @Override
    public void init(String alg, CryptoConfig config) {
        key = config.getKey();
        iv = config.getIv();
        CryptoAlgorithm cryptoAlgorithm = Optional.ofNullable(CryptoAlgorithm.fromCryptoAlgorithm(alg))
                .orElseThrow(() -> new McpException(String.format("加载内置加解密算法异常,algorithm:%s", alg)));
        pattern = cryptoAlgorithm.getPattern();
        algorithm = cryptoAlgorithm.getAlgorithm();
    }

    @Override
    public byte[] processDecryption(byte[] content) {
        return McpCryptoUtils.X.doSymmetricDecryption(algorithm, pattern, key, content, iv);
    }

    @Override
    public byte[] processEncryption(byte[] content) {
        return McpCryptoUtils.X.doSymmetricEncryption(algorithm, pattern, key, content, iv);
    }
}
