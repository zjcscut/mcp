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
