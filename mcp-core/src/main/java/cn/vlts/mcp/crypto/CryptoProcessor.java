package cn.vlts.mcp.crypto;

/**
 * 加解密处理器
 *
 * @author throwable
 * @version v1
 * @description 加解密处理器
 * @since 2023/12/19 15:56
 */
public interface CryptoProcessor {

    /**
     * 初始化配置
     *
     * @param algorithm 算法
     * @param config    加解密配置
     */
    default void init(String algorithm, CryptoConfig config) {

    }
}
