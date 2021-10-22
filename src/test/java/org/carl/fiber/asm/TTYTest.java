package org.carl.fiber.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class TTYTest {

    /**
     * JDB main函数主类
     */
    public static final String CLASS_NAME = "com.sun.tools.example.debug.tty.TTY";

    private static String[][] commandList = new String[2][10];

    @Test
    public void fun1() throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(System.getProperty("java.class.path"));
        if (classLoader instanceof URLClassLoader) {
            URL[] urLs = ((URLClassLoader) classLoader).getURLs();
            System.out.println(Arrays.toString(urLs));
        }
    }

    @Test
    public void testLoadMain() throws Exception {
        try {
            Class.forName(CLASS_NAME, false, this.getClass().getClassLoader());
        } catch (Exception e) {
            System.out.println("load error");
            return;
        }
        ClassReader reader = new ClassReader(CLASS_NAME);
        ClassWriter writer = new ClassWriter(reader, 0);
        ClassVisitor classVisitor = new JDBTTYClassVisitor(writer);
        reader.accept(classVisitor, ClassReader.SKIP_DEBUG);
        byte[] byteArray = writer.toByteArray();
        Files.write(Paths.get("TTY.class"), byteArray, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
    }

    private static class A {

        public FileFilter test() {

            return new FileFilter() {

                class C {
                }

                @Override
                public boolean accept(File pathname) {
                    return false;
                }
            };
        }

        private static class B {
        }
    }
}
