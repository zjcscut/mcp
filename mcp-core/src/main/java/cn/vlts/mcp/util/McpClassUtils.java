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

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author throwable
 * @version v1
 * @description 类工具
 * @since 2023/4/5 18:14
 */
public abstract class McpClassUtils {

    private static final String JAVA_PREFIX = "java.";

    private static final String JAVAX_PREFIX = "javax.";

    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_TYPE_MAP = new IdentityHashMap<>(8);

    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_WRAPPER_MAP = new IdentityHashMap<>(8);

    private static final Map<Class<?>, Class<?>> CONTAINER_TYPE_MAP = new IdentityHashMap<>(8);

    static {
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Boolean.class, boolean.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Byte.class, byte.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Character.class, char.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Double.class, double.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Float.class, float.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Integer.class, int.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Long.class, long.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Short.class, short.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Void.class, void.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.forEach((k, v) -> PRIMITIVE_TYPE_WRAPPER_MAP.put(v, k));
        CONTAINER_TYPE_MAP.put(Collection.class, ArrayList.class);
        CONTAINER_TYPE_MAP.put(List.class, ArrayList.class);
        CONTAINER_TYPE_MAP.put(Map.class, HashMap.class);
        CONTAINER_TYPE_MAP.put(SortedMap.class, TreeMap.class);
        CONTAINER_TYPE_MAP.put(NavigableMap.class, TreeMap.class);
        CONTAINER_TYPE_MAP.put(Set.class, HashSet.class);
        CONTAINER_TYPE_MAP.put(NavigableSet.class, TreeSet.class);
        CONTAINER_TYPE_MAP.put(ConcurrentMap.class, ConcurrentHashMap.class);
    }

    private McpClassUtils() {

    }

    public static boolean isPrimitive(Class<?> klass) {
        return klass.isPrimitive();
    }

    public static boolean isPrimitiveWrapper(Class<?> klass) {
        return isPrimitive(klass) || PRIMITIVE_WRAPPER_TYPE_MAP.containsKey(klass);
    }

    public static Class<?> getPrimitive(Class<?> klass) {
        return PRIMITIVE_WRAPPER_TYPE_MAP.get(klass);
    }

    public static Class<?> getPrimitiveWrapper(Class<?> klass) {
        return PRIMITIVE_TYPE_WRAPPER_MAP.get(klass);
    }

    public static boolean isJDKType(Class<?> clazz) {
        // 注意Primitive类型获取到的Package为NULL
        Package pkg = clazz.getPackage();
        return isPrimitiveWrapper(clazz) || (Objects.nonNull(pkg)
                && (McpStringUtils.startsWith(pkg.getName(), JAVAX_PREFIX)
                || McpStringUtils.startsWith(pkg.getName(), JAVA_PREFIX)))
                || McpStringUtils.startsWith(clazz.getName(), JAVAX_PREFIX)
                || McpStringUtils.startsWith(clazz.getName(), JAVA_PREFIX);
    }

    public static boolean isUserDefinedType(Class<?> clazz) {
        return !isJDKType(clazz);
    }

    public static boolean isAnnotationPresent(Class<?> clazz, Class<? extends Annotation> annotation) {
        return Objects.nonNull(clazz) && clazz.isAnnotationPresent(annotation);
    }

    public static Class<?> getContainerType(Class<?> clazz, Class<?> defaultClazz) {
        return CONTAINER_TYPE_MAP.getOrDefault(clazz, defaultClazz);
    }

    public static Class<?> guessContainerType(Class<?> clazz) {
        Class<?> containerClass = CONTAINER_TYPE_MAP.get(clazz);
        if (Objects.nonNull(containerClass)) {
            return containerClass;
        }
        if (List.class.isAssignableFrom(clazz)) {
            return CONTAINER_TYPE_MAP.get(List.class);
        }
        if (Map.class.isAssignableFrom(clazz)) {
            return CONTAINER_TYPE_MAP.get(Map.class);
        }
        if (Set.class.isAssignableFrom(clazz)) {
            return CONTAINER_TYPE_MAP.get(Set.class);
        }
        if (SortedSet.class.isAssignableFrom(clazz)) {
            return CONTAINER_TYPE_MAP.get(SortedSet.class);
        }
        if (SortedMap.class.isAssignableFrom(clazz)) {
            return CONTAINER_TYPE_MAP.get(SortedMap.class);
        }
        if (NavigableMap.class.isAssignableFrom(clazz)) {
            return CONTAINER_TYPE_MAP.get(NavigableMap.class);
        }
        if (NavigableSet.class.isAssignableFrom(clazz)) {
            return CONTAINER_TYPE_MAP.get(NavigableSet.class);
        }
        if (ConcurrentMap.class.isAssignableFrom(clazz)) {
            return CONTAINER_TYPE_MAP.get(ConcurrentMap.class);
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            return CONTAINER_TYPE_MAP.get(Collection.class);
        }
        return clazz;
    }

    public static void setContainerType(Class<?> interfaceType, Class<?> containerType) {
        CONTAINER_TYPE_MAP.putIfAbsent(interfaceType, containerType);
    }

    public static void replaceContainerType(Class<?> interfaceType, Class<?> containerType) {
        CONTAINER_TYPE_MAP.put(interfaceType, containerType);
    }

    public static Class<?>[] repeat(Class<?> clazz, int n) {
        Class<?>[] array = new Class<?>[n];
        Arrays.fill(array, clazz);
        return array;
    }
}
