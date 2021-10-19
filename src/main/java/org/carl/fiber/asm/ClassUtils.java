package org.carl.fiber.asm;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

public class ClassUtils {

    private static Method defineClass;

    static {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction() {
                public Object run() throws Exception {
                    Class cl = Class.forName("java.lang.ClassLoader");
                    ClassUtils.defineClass = cl.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
                    return null;
                }
            });
        } catch (PrivilegedActionException var1) {
            throw new RuntimeException("cannot initialize ClassPool", var1.getException());
        }
    }

    /**
     * 判断当前的Class是否存在
     *
     * @param className 指定的className
     * @return 返回当前类是否存在
     */
    public static boolean isPresent(String className) {
        try {
            Class.forName(className, false, Thread.currentThread().getContextClassLoader());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * 加载指定的ClassName
     *
     * @param className 指定的className
     * @return 返回加载后的className
     */
    public static Class<?> forName(String className) {
        try {
            return Class.forName(className, false, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成对应的Class文件
     *
     * @param className   指定的类名称
     * @param classBuffer 生成的二进制文件
     * @param classLoader 类加载器
     * @return 返回加载的Class文件
     */
    @SuppressWarnings("")
    public static Class<?> toClass(String className, byte[] classBuffer, ClassLoader classLoader) {
        defineClass.setAccessible(true);
        try {
            Object[] args = new Object[]{className, classBuffer, new Integer(0), new Integer(classBuffer.length)};
            return (Class<?>) defineClass.invoke(classLoader, args);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("can not load class: " + className);
        }
    }
}
