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
public class SpExternalReflectionProvider implements ExternalReflectionProvider {

    @Override
    public void doWithFields(Class<?> clazz, McpReflectionUtils.FieldCallback fc, McpReflectionUtils.FieldFilter ff) {
        ReflectionUtils.doWithFields(clazz, fc::doWith, ff::matches);
    }

    @Override
    public void doWithMethods(Class<?> clazz, McpReflectionUtils.MethodCallback mc, McpReflectionUtils.MethodFilter mf) {
        ReflectionUtils.doWithMethods(clazz, mc::doWith, mf::matches);
    }
}
