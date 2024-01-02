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

package cn.vlts.mcp.util;

import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import static org.apache.ibatis.reflection.SystemMetaObject.DEFAULT_OBJECT_FACTORY;
import static org.apache.ibatis.reflection.SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY;

/**
 * Mybatis工具类
 *
 * @author throwable
 * @version v1
 * @description Mybatis工具类
 * @since 2023/12/25 15:40
 */
public enum MybatisUtils {

    /**
     *
     */
    X;

    private static final String H = "h";

    private static final String TARGET = "target";

    private static final DefaultReflectorFactory REFLECTOR_FACTORY = new DefaultReflectorFactory();

    /**
     * 创建目标元数据对象
     *
     * @param target target
     * @return meta object
     */
    public MetaObject newMetaObject(Object target) {
        return MetaObject.forObject(target, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY,
                REFLECTOR_FACTORY);
    }

    /**
     * 提取目标元数据对象
     *
     * @param target target
     * @return meta object
     */
    public MetaObject extractTargetMetaObject(Object target) {
        MetaObject metaObject = SystemMetaObject.forObject(target);
        while (metaObject.hasGetter(H)) {
            Object object = metaObject.getValue(H);
            metaObject = newMetaObject(object);
        }
        while (metaObject.hasGetter(TARGET)) {
            Object object = metaObject.getValue(TARGET);
            metaObject = newMetaObject(object);
        }
        return metaObject;
    }
}
