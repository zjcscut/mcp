package cn.vlts.mcp.spi;

/**
 * 引用类型加解密匹配器
 *
 * @author throwable
 * @version v1
 * @description 引用类型加解密匹配器
 * @since 2023/12/22 00:40
 */
@FunctionalInterface
public interface ReferenceCryptoMatcher {

    ReferenceCryptoMatcher DEFAULT = (refType, ref, encryptMode) -> true;

    /**
     * 是否匹配
     *
     * @param refType     refType
     * @param ref         ref
     * @param encryptMode encryptMode
     * @return match result
     */
    boolean match(Class<?> refType, Object ref, boolean encryptMode);
}
