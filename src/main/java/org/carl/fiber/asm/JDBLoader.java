package org.carl.fiber.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public class JDBLoader {
    /**
     * JDB 的主类
     */
    public static final String JDB_CLASS_NAME = "com.sun.tools.example.debug.tty.TTY";

    /**
     * 输出信息的对应前缀
     */
    public static final String[] JDB_PROMPT_CLASSES = {
        "com.sun.tools.example.debug.tty.TTYResources",
        "com.sun.tools.example.debug.tty.TTYResources_ja",
        "com.sun.tools.example.debug.tty.TTYResources_zh_CN"};

    /**
     * 修改 调试中需要使用的JDB类
     *
     * @return 返回生成的类
     */
    public static Class<?> modifyJDBClass() {
        Class<?> loadClass;
        try {
            ClassReader reader = new ClassReader(JDB_CLASS_NAME);
            ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            ClassVisitor classVisitor = new JDBTTYClassVisitor(writer);
            reader.accept(classVisitor, ClassReader.SKIP_DEBUG);
            loadClass = ClassUtils.toClass(JDB_CLASS_NAME, writer.toByteArray(), Thread.currentThread().getContextClassLoader());
        } catch (Exception e) {
            loadClass = ClassUtils.forName(JDB_CLASS_NAME);
        }

        // 加载处理资源输出的文件
        modifyJDBResourcesClass();

        return loadClass;
    }

    /**
     * 修改所有的资源类文件
     */
    private static void modifyJDBResourcesClass() {
        for (String className : JDB_PROMPT_CLASSES) {
            try {
                ClassReader reader = new ClassReader(className);
                ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                ClassVisitor classVisitor = new JDBResourceClassVisitor(writer);
                reader.accept(classVisitor, ClassReader.SKIP_DEBUG);
                ClassUtils.toClass(className, writer.toByteArray(), Thread.currentThread().getContextClassLoader());
            } catch (Exception e) {
                ClassUtils.forName(className);
            }
        }
    }
}
