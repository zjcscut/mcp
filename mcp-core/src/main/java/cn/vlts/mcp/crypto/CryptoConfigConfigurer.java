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

import cn.vlts.mcp.spi.CryptoTarget;

/**
 * 加解密配置配置器
 *
 * @author throwable
 * @version v1
 * @description 加解密配置配置器
 * @since 2023/12/20 20:31
 */

@FunctionalInterface
public interface CryptoConfigConfigurer {

    /**
     * 匹配
     *
     * @param cryptoTarget cryptoTarget
     * @return match result
     */
    default boolean match(CryptoTarget cryptoTarget) {
        return true;
    }

    /**
     * 排序号
     *
     * @return order
     */
    default int order() {
        return Integer.MAX_VALUE;
    }

    /**
     * 应用
     *
     * @param cryptoTarget cryptoTarget
     * @param config       config
     */
    void apply(CryptoTarget cryptoTarget, CryptoConfig config);
}
