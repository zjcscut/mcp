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
 * 字符串加密处理器
 *
 * @author throwable
 * @version v1
 * @description 字符串加密处理器
 * @since 2023/12/19 15:58
 */
@FunctionalInterface
public interface StringEncryptProcessor {

    /**
     * 加密前回调
     *
     * @param content content
     * @return result
     */
    default String processBeforeEncryption(String content) {
        return content;
    }

    /**
     * 处理加密
     *
     * @param content 明文内容
     * @return 密文内容
     */
    String processEncryption(String content);

    /**
     * 加密后回调
     *
     * @param content content
     * @return result
     */
    default String processAfterEncryption(String content) {
        return content;
    }
}
