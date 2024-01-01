package cn.vlts.mcp;


import cn.vlts.mcp.util.ExternalReflectionProvider;
import cn.vlts.mcp.util.McpReflectionUtils;
import org.springframework.util.ReflectionUtils;

/**
 * Spring外部放射提供者 - 这里使用Spring提供的放射元数据缓存
 *
 * @author throwable
 * @version v1
 * @description Spring外部放射提供者
 * @since 2023/12/22 10:11
 */
public class SpringExternalReflectionProvider implements ExternalReflectionProvider {

    @Override
    public void doWithFields(Class<?> clazz, McpReflectionUtils.FieldCallback fc, McpReflectionUtils.FieldFilter ff) {
        ReflectionUtils.doWithFields(clazz, fc::doWith, ff::matches);
    }

    @Override
    public void doWithMethods(Class<?> clazz, McpReflectionUtils.MethodCallback mc, McpReflectionUtils.MethodFilter mf) {
        ReflectionUtils.doWithMethods(clazz, mc::doWith, mf::matches);
    }
}
