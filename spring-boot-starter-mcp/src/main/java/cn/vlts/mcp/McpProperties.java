package cn.vlts.mcp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import cn.vlts.mcp.common.Mode;

import java.util.List;

/**
 * MCP属性
 *
 * @author throwable
 * @version v1
 * @description MCP属性
 * @since 2023/12/21 11:03
 */
@ConfigurationProperties(prefix = "mcp")
public class McpProperties {

    private Boolean enabled = Boolean.TRUE;

    private Boolean fastFail = Boolean.TRUE;

    private Mode mode = Mode.REFLECTION;

    private String globalKey;

    private String globalIv;

    private String globalPubKey;

    private String globalPriKey;

    private String globalCryptoAlgorithm;

    private String globalCryptoProcessor;

    private List<String> typePackages;

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

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public String getGlobalKey() {
        return globalKey;
    }

    public void setGlobalKey(String globalKey) {
        this.globalKey = globalKey;
    }

    public String getGlobalIv() {
        return globalIv;
    }

    public void setGlobalIv(String globalIv) {
        this.globalIv = globalIv;
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

    public String getGlobalCryptoProcessor() {
        return globalCryptoProcessor;
    }

    public void setGlobalCryptoProcessor(String globalCryptoProcessor) {
        this.globalCryptoProcessor = globalCryptoProcessor;
    }

    public List<String> getTypePackages() {
        return typePackages;
    }

    public void setTypePackages(List<String> typePackages) {
        this.typePackages = typePackages;
    }

    public String getGlobalCryptoAlgorithm() {
        return globalCryptoAlgorithm;
    }

    public void setGlobalCryptoAlgorithm(String globalCryptoAlgorithm) {
        this.globalCryptoAlgorithm = globalCryptoAlgorithm;
    }
}
