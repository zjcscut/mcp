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


import cn.vlts.mcp.crypto.DuplexStringCryptoProcessor;

/**
 * 全局配置提供者
 *
 * @author throwable
 * @version v1
 * @description 全局配置提供者
 * @since 2023/12/21 15:41
 */
public interface GlobalConfigProvider {

    /**
     * 获取全局加解密密钥
     *
     * @return global key
     */
    String getGlobalKey();

    /**
     * 获取全局加解密向量
     *
     * @return global iv
     */
    String getGlobalIv();

    /**
     * 获取全局加解密公钥
     *
     * @return global public key
     */
    String getGlobalPubKey();

    /**
     * 获取全局加解密私钥
     *
     * @return global private key
     */
    String getGlobalPriKey();

    /**
     * 获取全局加解密处理器
     *
     * @return global crypto processor
     */
    DuplexStringCryptoProcessor getGlobalCryptoProcessor();
}
