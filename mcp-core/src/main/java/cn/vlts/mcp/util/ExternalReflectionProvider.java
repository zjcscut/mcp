package cn.vlts.mcp.util;

/**
 * 外部反射处理提供者 - 用于SPI从外部加载反射处理的具体实现
 *
 * @author throwable
 * @version v1
 * @description 外部反射处理提供者
 * @since 2023/12/22 9:56
 */
public interface ExternalReflectionProvider {

    /**
     * 处理某个类所有字段
     *
     * @param clazz clazz
     * @param fc    fc
     * @param ff    ff
     */
    void doWithFields(Class<?> clazz,
                      McpReflectionUtils.FieldCallback fc,
                      McpReflectionUtils.FieldFilter ff);

    /**
     * 处理某个类所有方法
     *
     * @param clazz clazz
     * @param mc    mc
     * @param mf    mf
     */
    void doWithMethods(Class<?> clazz,
                       McpReflectionUtils.MethodCallback mc,
                       McpReflectionUtils.MethodFilter mf);
}
