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

/**
 * 加解密处理器工厂
 *
 * @author throwable
 * @version v1
 * @description 加解密处理器工厂
 * @since 2023/12/21 9:52
 */
public interface CryptoProcessorFactory {

    /**
     * 加载内置加解密处理器 - 不存在则进行创建
     *
     * @param algorithm algorithm
     * @param config    config
     * @return internal crypto processor
     */
    DuplexStringCryptoProcessor loadBuildInCryptoProcessor(CryptoAlgorithm algorithm,
                                                           CryptoConfig config);

    /**
     * 加载自定义加解密处理器 - 不存在则进行创建
     *
     * @param type   type
     * @param config config
     * @return custom crypto processor
     */
    DuplexStringCryptoProcessor loadCustomCryptoProcessor(Class<? extends DuplexStringCryptoProcessor> type,
                                                          CryptoConfig config);
}
