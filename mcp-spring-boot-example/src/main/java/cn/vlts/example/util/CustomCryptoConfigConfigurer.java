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

package cn.vlts.example.util;

import cn.vlts.mcp.crypto.CryptoConfig;
import cn.vlts.mcp.crypto.CryptoConfigConfigurer;
import cn.vlts.mcp.spi.CryptoTarget;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 自定义加解密配置配置器
 *
 * @author throwable
 * @version v1
 * @description
 * @since 2024/1/2 11:46
 */
@Slf4j
@Component
public class CustomCryptoConfigConfigurer implements CryptoConfigConfigurer {

    @Override
    public void apply(CryptoTarget cryptoTarget, CryptoConfig config) {
        if (cryptoTarget.isJavaField()) {
            if (Objects.equals(cryptoTarget.fieldName(), "foo")) {
                config.setKey("1111111122222222");
            }
        }
    }
}
