package cn.vlts.mcp.crypto;

import java.lang.reflect.Field;

/**
 * 字段加解密处理器选择器
 *
 * @author throwable
 * @version v1
 * @description 字段加解密处理器选择器
 * @since 2023/12/21 14:48
 */
@FunctionalInterface
public interface FieldCryptoProcessorChooser {

    /**
     * 选择字段加解密处理器
     *
     * @param field field
     * @return crypto processor
     */
    DuplexStringCryptoProcessor chooseFieldCryptoProcessor(Field field);
}
