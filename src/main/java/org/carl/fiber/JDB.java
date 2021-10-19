package org.carl.fiber;

import org.carl.fiber.asm.JDBLoader;

import java.lang.reflect.Method;

/**
 * 用以增强 JDB 的主类
 */
public class JDB {

    /**
     * JDB调用的主函数
     */
    public static final String JDB_MAIN_METHOD_NAME = "main";

    public static void main(String[] args) throws Exception {
        Class<?> loadClass = JDBLoader.modifyJDBClass();
        Method mainMethod = loadClass.getMethod(JDB_MAIN_METHOD_NAME, String[].class);
        mainMethod.invoke(null, new Object[]{args});
    }
}
