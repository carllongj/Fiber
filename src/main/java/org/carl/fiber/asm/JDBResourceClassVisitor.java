package org.carl.fiber.asm;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Objects;

public class JDBResourceClassVisitor extends ClassVisitor {

    /**
     * 修改其 getContents 函数的返回值
     */
    private static final String METHOD_NAME = "getContents";

    public JDBResourceClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM7, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        // 匹配到符合的函数
        if (Objects.equals(METHOD_NAME, name)) {
            return new JDBResourcesGetContentsMethodVisitor(mv);
        }
        return mv;
    }
}
