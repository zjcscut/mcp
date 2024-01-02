package cn.vlts.example.util;

import cn.vlts.mcp.util.ExternalReflectionProvider;
import cn.vlts.mcp.util.McpReflectionUtils;
import org.springframework.util.ReflectionUtils;

/**
 * 自定义反射提供者
 *
 * @author throwable
 * @version v1
 * @description 自定义反射提供者
 * @since 2024/1/2 9:56
 */
public class CustomReflectionProvider implements ExternalReflectionProvider {

    @Override
    public void doWithFields(Class<?> clazz, McpReflectionUtils.FieldCallback fc, McpReflectionUtils.FieldFilter ff) {
        ReflectionUtils.doWithFields(clazz, fc::doWith, ff::matches);
    }

    @Override
    public void doWithMethods(Class<?> clazz, McpReflectionUtils.MethodCallback mc, McpReflectionUtils.MethodFilter mf) {
        ReflectionUtils.doWithMethods(clazz, mc::doWith, mf::matches);
    }
}
