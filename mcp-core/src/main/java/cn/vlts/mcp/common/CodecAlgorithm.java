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

package cn.vlts.mcp.common;


import cn.vlts.mcp.codec.Base64CodecProcessor;
import cn.vlts.mcp.codec.CodecProcessor;
import cn.vlts.mcp.codec.HexCodecProcessor;
import cn.vlts.mcp.codec.RawCodecProcessor;

/**
 * 编码解码算法
 *
 * @author throwable
 * @version v1
 * @description 编码解码算法
 * @since 2023/12/19 15:48
 */
public enum CodecAlgorithm {

    /**
     * 原始
     */
    RAW(RawCodecProcessor.INSTANCE),

    /**
     * 十六进制
     */
    HEX(HexCodecProcessor.INSTANCE),

    /**
     * BASE64
     */
    BASE64(Base64CodecProcessor.INSTANCE),

    ;

    private final CodecProcessor codecProcessor;

    CodecAlgorithm(CodecProcessor codecProcessor) {
        this.codecProcessor = codecProcessor;
    }

    public CodecProcessor getCodecProcessor() {
        return codecProcessor;
    }
}
