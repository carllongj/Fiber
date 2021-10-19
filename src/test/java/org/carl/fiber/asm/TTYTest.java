package org.carl.fiber.asm;

import org.carl.fiber.jline.JDBConsoleReader;
import org.carl.fiber.jline.JDBConsoleReaderProvider;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TTYTest {

    /**
     * JDB main函数主类
     */
    public static final String CLASS_NAME = "com.sun.tools.example.debug.tty.TTY";

    private static String[][] commandList = new String[2][10];

    public void fun1() throws Exception {
        JDBConsoleReader consoleReader = JDBConsoleReaderProvider.provide(commandList);
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
}
