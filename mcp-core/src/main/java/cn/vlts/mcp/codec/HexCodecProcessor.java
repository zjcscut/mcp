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

package cn.vlts.mcp.codec;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;

/**
 * 十六进制编码解码处理器
 *
 * @author throwable
 * @version v1
 * @description 十六进制编码解码处理器
 * @since 2023/12/19 20:34
 */
public class HexCodecProcessor implements CodecProcessor {

    public static final CodecProcessor INSTANCE = new HexCodecProcessor();

    @Override
    public byte[] encode(byte[] bytes) {
        return new String(Hex.encodeHex(bytes)).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] decode(byte[] bytes) {
        String chars = new String(bytes, StandardCharsets.UTF_8);
        try {
            return Hex.decodeHex(chars);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String encodeToString(byte[] content) {
        return Hex.encodeHexString(content);
    }

    @Override
    public byte[] decodeFromString(String content) {
        try {
            return Hex.decodeHex(content);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
