package org.carl.fiber.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Objects;

/**
 * 替换资源类的输出 prompt 符号
 */
public class JDBResourcesGetContentsMethodVisitor extends MethodVisitor {

    /**
     * 用于替换的资源的JDB KEY
     */
    private static final String JDB_RESOURCE_KEY = "jdb prompt with no current thread";

    /**
     * 是否访问到对应的KEY
     */
    private boolean flag;

    public JDBResourcesGetContentsMethodVisitor(MethodVisitor methodVisitor) {
        super(Opcodes.ASM7, methodVisitor);
    }

    @Override
    public void visitLdcInsn(Object value) {
        // 判断之前是否已经标识开启替换
        if (this.flag) {
            // 替换掉对应的"> "符号
            super.visitLdcInsn("");
            this.flag = false;
            return;
        }

        // 是否为指定的 "jdb prompt with no current thread"
        if (Objects.equals(JDB_RESOURCE_KEY, value)) {
            this.flag = true;
        }
        super.visitLdcInsn(value);
    }
}
