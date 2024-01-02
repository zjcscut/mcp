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

/**
 * 字符串解密处理器
 *
 * @author throwable
 * @version v1
 * @description 字符串解密处理器
 * @since 2023/12/19 15:58
 */
@FunctionalInterface
public interface StringDecryptProcessor {

    /**
     * 解密前回调
     *
     * @param content content
     * @return result
     */
    default String processBeforeDecryption(String content) {
        return content;
    }

    /**
     * 处理解密
     *
     * @param content 密文内容
     * @return 明文内容
     */
    String processDecryption(String content);

    /**
     * 解密后回调
     *
     * @param content content
     * @return result
     */
    default String processAfterDecryption(String content) {
        return content;
    }
}
