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

package cn.vlts.mcp.config;


import cn.vlts.mcp.common.Mode;
import cn.vlts.mcp.crypto.DuplexStringCryptoProcessor;

/**
 * MCP配置
 *
 * @author throwable
 * @version v1
 * @description MCP配置
 * @since 2023/12/20 14:57
 */
public class McpConfig {

    private Boolean enabled = Boolean.TRUE;

    private Boolean fastFail = Boolean.TRUE;

    private Mode mode = Mode.REFLECTION;

    private String globalKey;

    private String globalIv;

    private String globalPubKey;

    private String globalPriKey;

    private String globalCryptoAlgorithm;

    private Class<? extends DuplexStringCryptoProcessor> globalCryptoProcessor;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getFastFail() {
        return fastFail;
    }

    public void setFastFail(Boolean fastFail) {
        this.fastFail = fastFail;
    }

    public String getGlobalKey() {
        return globalKey;
    }

    public void setGlobalKey(String globalKey) {
        this.globalKey = globalKey;
    }

    public String getGlobalPubKey() {
        return globalPubKey;
    }

    public void setGlobalPubKey(String globalPubKey) {
        this.globalPubKey = globalPubKey;
    }

    public String getGlobalPriKey() {
        return globalPriKey;
    }

    public void setGlobalPriKey(String globalPriKey) {
        this.globalPriKey = globalPriKey;
    }

    public Class<? extends DuplexStringCryptoProcessor> getGlobalCryptoProcessor() {
        return globalCryptoProcessor;
    }

    public void setGlobalCryptoProcessor(Class<? extends DuplexStringCryptoProcessor> globalCryptoProcessor) {
        this.globalCryptoProcessor = globalCryptoProcessor;
    }

    public String getGlobalIv() {
        return globalIv;
    }

    public void setGlobalIv(String globalIv) {
        this.globalIv = globalIv;
    }

    public String getGlobalCryptoAlgorithm() {
        return globalCryptoAlgorithm;
    }

    public void setGlobalCryptoAlgorithm(String globalCryptoAlgorithm) {
        this.globalCryptoAlgorithm = globalCryptoAlgorithm;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }
}
