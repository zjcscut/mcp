package cn.vlts.mcp.util;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 自定义反射工具
 *
 * @author throwable
 * @version v1
 * @description 自定义反射工具
 * @since 2023/4/4 20:52
 */
public abstract class McpReflectionUtils {

    public static final MethodFilter USER_DECLARED_METHODS =
            (method -> !method.isBridge() && !method.isSynthetic());

    private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];

    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    private static final String CLONE_METHOD = "clone";

    private static final String DOT = ".";

    private static ExternalReflectionProvider EXTERNAL_PROVIDER = null;

    private McpReflectionUtils() {

    }

    public static void attachExternalProvider(ExternalReflectionProvider externalProvider) {
        EXTERNAL_PROVIDER = externalProvider;
    }

    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())
                || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) ||
                !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    public static void makeAccessible(Constructor<?> ctor) {
        if ((!Modifier.isPublic(ctor.getModifiers()) ||
                !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return accessibleConstructor(clazz, EMPTY_CLASS_ARRAY).newInstance(EMPTY_OBJECT_ARRAY);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T> Constructor<T> accessibleConstructor(Class<T> clazz, Class<?>... parameterTypes) {
        try {
            Constructor<T> ctor = clazz.getDeclaredConstructor(parameterTypes);
            makeAccessible(ctor);
            return ctor;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static void doWithFields(Class<?> clazz, FieldCallback fc) {
        doWithFields(clazz, fc, null);
    }

    public static void doWithFields(Class<?> clazz, FieldCallback fc, FieldFilter ff) {
        Class<?> targetClass = clazz;
        if (Objects.nonNull(EXTERNAL_PROVIDER)) {
            EXTERNAL_PROVIDER.doWithFields(clazz, fc, ff);
        } else {
            do {
                Field[] fields = targetClass.getDeclaredFields();
                for (Field field : fields) {
                    if (Objects.isNull(ff) || ff.matches(field)) {
                        try {
                            fc.doWith(field);
                        } catch (IllegalAccessException e) {
                            throw new IllegalStateException("Not allowed to access field '" + field.getName() + "': " + e);
                        }
                    }
                }
                targetClass = targetClass.getSuperclass();
            } while (targetClass != null && targetClass != Object.class);
        }
    }

    public static void doWithMethods(Class<?> clazz, MethodCallback mc) {
        doWithMethods(clazz, mc, null);
    }

    public static void doWithMethods(Class<?> clazz, MethodCallback mc, MethodFilter mf) {
        if (Objects.nonNull(EXTERNAL_PROVIDER)) {
            EXTERNAL_PROVIDER.doWithMethods(clazz, mc, mf);
        } else {
            // Keep backing up the inheritance hierarchy.
            Method[] methods = getDeclaredMethods(clazz, false);
            for (Method method : methods) {
                if (Objects.nonNull(mf) && !mf.matches(method)) {
                    continue;
                }
                try {
                    mc.doWith(method);
                } catch (IllegalAccessException ex) {
                    throw new IllegalStateException("Not allowed to access method '" + method.getName() + "': " + ex);
                }
            }
            if (clazz.getSuperclass() != null && (mf != USER_DECLARED_METHODS || clazz.getSuperclass() != Object.class)) {
                doWithMethods(clazz.getSuperclass(), mc, mf);
            } else if (clazz.isInterface()) {
                for (Class<?> superIfc : clazz.getInterfaces()) {
                    doWithMethods(superIfc, mc, mf);
                }
            }
        }
    }

    private static Method[] getDeclaredMethods(Class<?> clazz, boolean defensive) {
        Method[] result;
        try {
            Method[] declaredMethods = clazz.getDeclaredMethods();
            List<Method> defaultMethods = findConcreteMethodsOnInterfaces(clazz);
            if (defaultMethods != null) {
                result = new Method[declaredMethods.length + defaultMethods.size()];
                System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
                int index = declaredMethods.length;
                for (Method defaultMethod : defaultMethods) {
                    result[index] = defaultMethod;
                    index++;
                }
            } else {
                result = declaredMethods;
            }
        } catch (Throwable ex) {
            throw new IllegalStateException("Failed to introspect Class [" + clazz.getName() +
                    "] from ClassLoader [" + clazz.getClassLoader() + "]", ex);
        }
        return (result.length == 0 || !defensive) ? result : result.clone();
    }

    private static List<Method> findConcreteMethodsOnInterfaces(Class<?> clazz) {
        List<Method> result = null;
        for (Class<?> ifc : clazz.getInterfaces()) {
            for (Method ifcMethod : ifc.getMethods()) {
                if (!Modifier.isAbstract(ifcMethod.getModifiers())) {
                    if (result == null) {
                        result = new ArrayList<>();
                    }
                    result.add(ifcMethod);
                }
            }
        }
        return result;
    }

    public static Method findMethod(Class<?> clazz, String name) {
        return findMethod(clazz, name, EMPTY_CLASS_ARRAY);
    }

    public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
        if (Objects.nonNull(EXTERNAL_PROVIDER)) {
            MethodHolder methodHolder = new MethodHolder();
            EXTERNAL_PROVIDER.doWithMethods(clazz, methodHolder::setMethod,
                    method -> name.equals(method.getName()) && (Objects.isNull(paramTypes) || hasSameParams(method, paramTypes)));
            if (methodHolder.hasMethod()) {
                return methodHolder.getMethod();
            }
            return null;
        }
        Class<?> searchType = clazz;
        while (searchType != null) {
            Method[] methods = (searchType.isInterface() ? searchType.getMethods() :
                    getDeclaredMethods(searchType, false));
            for (Method method : methods) {
                if (name.equals(method.getName()) && (paramTypes == null || hasSameParams(method, paramTypes))) {
                    return method;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    public static Field findField(Class<?> clazz, String name) {
        return findField(clazz, name, null);
    }

    public static Field findField(Class<?> clazz, String name, Class<?> type) {
        if (Objects.nonNull(EXTERNAL_PROVIDER)) {
            FieldHolder fieldHolder = new FieldHolder();
            EXTERNAL_PROVIDER.doWithFields(clazz, fieldHolder::setField,
                    field -> (Objects.isNull(name) || name.equals(field.getName())) &&
                            (Objects.isNull(type) || type.equals(field.getType())));
            if (fieldHolder.hasField()) {
                return fieldHolder.getField();
            }
            return null;
        }
        Class<?> searchType = clazz;
        while (Object.class != searchType && searchType != null) {
            Field[] fields = getDeclaredFields(searchType);
            for (Field field : fields) {
                if ((name == null || name.equals(field.getName())) &&
                        (type == null || type.equals(field.getType()))) {
                    return field;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    private static Field[] getDeclaredFields(Class<?> clazz) {
        try {
            return clazz.getDeclaredFields();
        } catch (Throwable ex) {
            throw new IllegalStateException("Failed to introspect Class [" + clazz.getName() +
                    "] from ClassLoader [" + clazz.getClassLoader() + "]", ex);
        }
    }

    public static Object invokeMethod(Method method, Object target) {
        return invokeMethod(method, target, EMPTY_OBJECT_ARRAY);
    }

    public static Object invokeMethod(Method method, Object target, Object... args) {
        try {
            makeAccessible(method);
            return method.invoke(target, args);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static Method findCloneMethod(Class<?> clazz) {
        return findMethod(clazz, CLONE_METHOD);
    }

    private static boolean hasSameParams(Method method, Class<?>[] paramTypes) {
        return (paramTypes.length == method.getParameterCount() &&
                Arrays.equals(paramTypes, method.getParameterTypes()));
    }

    public static boolean isStaticFinal(Field field) {
        return Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers());
    }

    public static String displayField(Field field) {
        return field.getDeclaringClass().getName().concat(DOT).concat(field.getName());
    }

    public static void setFieldValue(Object instance, Field field, Object value) {
        try {
            makeAccessible(field);
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Object getFieldValue(Object instance, Field field) {
        try {
            makeAccessible(field);
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    @FunctionalInterface
    public interface FieldFilter {

        boolean matches(Field field);
    }

    @FunctionalInterface
    public interface FieldCallback {

        void doWith(Field field) throws IllegalArgumentException, IllegalAccessException;
    }

    @FunctionalInterface
    public interface MethodCallback {

        void doWith(Method method) throws IllegalArgumentException, IllegalAccessException;
    }

    @FunctionalInterface
    public interface MethodFilter {

        boolean matches(Method method);
    }

    private static class FieldHolder {

        private Field field;

        public boolean hasField() {
            return Objects.nonNull(this.field);
        }

        public void setField(Field field) {
            this.field = field;
        }

        public Field getField() {
            return this.field;
        }
    }

    private static class MethodHolder {

        private Method method;

        public boolean hasMethod() {
            return Objects.nonNull(this.method);
        }

        public void setMethod(Method method) {
            this.method = method;
        }

        public Method getMethod() {
            return this.method;
        }
    }
}
