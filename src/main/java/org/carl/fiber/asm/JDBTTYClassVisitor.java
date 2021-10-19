package org.carl.fiber.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Objects;

import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.ICONST_2;
import static org.objectweb.asm.Opcodes.ICONST_5;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.IFNONNULL;
import static org.objectweb.asm.Opcodes.IFNULL;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.POP;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;


public class JDBTTYClassVisitor extends ClassVisitor {

    /**
     * 构造器函数名称
     */
    public static final String CONSTRUCTOR_NAME = "<init>";

    /**
     * 指定的构造器描述符名称
     */
    public static final String CONSTRUCTOR_DESCRIPTOR = "()V";

    public JDBTTYClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM7, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        // 修改构造器中的函数
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

        // 构造器则修改其内容
        if (Objects.equals(name, CONSTRUCTOR_NAME) && Objects.equals(descriptor, CONSTRUCTOR_DESCRIPTOR)) {
            mv.visitCode();
            Label l0 = new Label();
            Label l1 = new Label();
            mv.visitTryCatchBlock(l0, l1, l1, "com/sun/jdi/VMDisconnectedException");
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLineNumber(705, l2);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLineNumber(46, l3);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitInsn(ACONST_NULL);
            mv.visitFieldInsn(PUTFIELD, "com/sun/tools/example/debug/tty/TTY", "handler", "Lcom/sun/tools/example/debug/tty/EventHandler;");
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitLineNumber(51, l4);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitTypeInsn(NEW, "java/util/ArrayList");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);
            mv.visitFieldInsn(PUTFIELD, "com/sun/tools/example/debug/tty/TTY", "monitorCommands", "Ljava/util/List;");
            Label l5 = new Label();
            mv.visitLabel(l5);
            mv.visitLineNumber(52, l5);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitInsn(ICONST_0);
            mv.visitFieldInsn(PUTFIELD, "com/sun/tools/example/debug/tty/TTY", "monitorCount", "I");
            Label l6 = new Label();
            mv.visitLabel(l6);
            mv.visitLineNumber(59, l6);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitInsn(ICONST_0);
            mv.visitFieldInsn(PUTFIELD, "com/sun/tools/example/debug/tty/TTY", "shuttingDown", "Z");
            Label l7 = new Label();
            mv.visitLabel(l7);
            mv.visitLineNumber(707, l7);
            mv.visitLdcInsn("Initializing progname");
            mv.visitLdcInsn("jdb");
            mv.visitMethodInsn(INVOKESTATIC, "com/sun/tools/example/debug/tty/MessageOutput", "println", "(Ljava/lang/String;Ljava/lang/String;)V", false);
            Label l8 = new Label();
            mv.visitLabel(l8);
            mv.visitLineNumber(709, l8);
            mv.visitMethodInsn(INVOKESTATIC, "com/sun/tools/example/debug/tty/Env", "connection", "()Lcom/sun/tools/example/debug/tty/VMConnection;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/sun/tools/example/debug/tty/VMConnection", "isOpen", "()Z", false);
            mv.visitJumpInsn(IFEQ, l0);
            mv.visitMethodInsn(INVOKESTATIC, "com/sun/tools/example/debug/tty/Env", "vm", "()Lcom/sun/jdi/VirtualMachine;", false);
            mv.visitMethodInsn(INVOKEINTERFACE, "com/sun/jdi/VirtualMachine", "canBeModified", "()Z", true);
            mv.visitJumpInsn(IFEQ, l0);
            Label l9 = new Label();
            mv.visitLabel(l9);
            mv.visitLineNumber(715, l9);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitTypeInsn(NEW, "com/sun/tools/example/debug/tty/EventHandler");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitInsn(ICONST_1);
            mv.visitMethodInsn(INVOKESPECIAL, "com/sun/tools/example/debug/tty/EventHandler", "<init>", "(Lcom/sun/tools/example/debug/tty/EventNotifier;Z)V", false);
            mv.visitFieldInsn(PUTFIELD, "com/sun/tools/example/debug/tty/TTY", "handler", "Lcom/sun/tools/example/debug/tty/EventHandler;");
            mv.visitLabel(l0);
            mv.visitLineNumber(718, l0);
            mv.visitFrame(Opcodes.F_FULL, 1, new Object[]{"com/sun/tools/example/debug/tty/TTY"}, 0, new Object[]{});

            /* 替换逻辑,此处加入ConsoleReader处理 (START) */
            mv.visitFieldInsn(GETSTATIC, "com/sun/tools/example/debug/tty/TTY", "commandList", "[[Ljava/lang/String;");
            mv.visitMethodInsn(INVOKESTATIC, "org/carl/fiber/jline/JDBConsoleReaderProvider", "provide", "([[Ljava/lang/String;)Lorg/carl/fiber/jline/JDBConsoleReader;", false);
            // 此处是将BufferedReader 的变量替换,但位置不变
            mv.visitVarInsn(ASTORE, 1);
            /* 替换逻辑,此处加入ConsoleReader处理 (END) */

            Label l10 = new Label();
            mv.visitLabel(l10);
            mv.visitLineNumber(721, l10);
            mv.visitInsn(ACONST_NULL);
            mv.visitVarInsn(ASTORE, 2);
            Label l11 = new Label();
            mv.visitLabel(l11);
            mv.visitLineNumber(723, l11);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
            mv.visitInsn(ICONST_5);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Thread", "setPriority", "(I)V", false);
            Label l12 = new Label();
            mv.visitLabel(l12);
            mv.visitLineNumber(743, l12);
            mv.visitLdcInsn("user.home");
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "getProperty", "(Ljava/lang/String;)Ljava/lang/String;", false);
            mv.visitVarInsn(ASTORE, 3);
            Label l13 = new Label();
            mv.visitLabel(l13);
            mv.visitLineNumber(746, l13);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitLdcInsn("jdb.ini");
            mv.visitInsn(ACONST_NULL);
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/sun/tools/example/debug/tty/TTY", "readStartupCommandFile", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", false);
            mv.visitInsn(DUP);
            mv.visitVarInsn(ASTORE, 4);
            Label l14 = new Label();
            mv.visitJumpInsn(IFNONNULL, l14);
            Label l15 = new Label();
            mv.visitLabel(l15);
            mv.visitLineNumber(748, l15);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitLdcInsn(".jdbrc");
            mv.visitInsn(ACONST_NULL);
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/sun/tools/example/debug/tty/TTY", "readStartupCommandFile", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", false);
            mv.visitVarInsn(ASTORE, 4);
            mv.visitLabel(l14);
            mv.visitLineNumber(751, l14);
            mv.visitFrame(Opcodes.F_FULL, 5, new Object[]{"com/sun/tools/example/debug/tty/TTY", "java/io/BufferedReader", "java/lang/String", "java/lang/String", "java/lang/String"}, 0, new Object[]{});
            mv.visitLdcInsn("user.dir");
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "getProperty", "(Ljava/lang/String;)Ljava/lang/String;", false);
            mv.visitVarInsn(ASTORE, 5);
            Label l16 = new Label();
            mv.visitLabel(l16);
            mv.visitLineNumber(752, l16);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 5);
            mv.visitLdcInsn("jdb.ini");
            mv.visitVarInsn(ALOAD, 4);
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/sun/tools/example/debug/tty/TTY", "readStartupCommandFile", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", false);
            Label l17 = new Label();
            mv.visitJumpInsn(IFNONNULL, l17);
            Label l18 = new Label();
            mv.visitLabel(l18);
            mv.visitLineNumber(754, l18);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 5);
            mv.visitLdcInsn(".jdbrc");
            mv.visitVarInsn(ALOAD, 4);
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/sun/tools/example/debug/tty/TTY", "readStartupCommandFile", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", false);
            mv.visitInsn(POP);
            mv.visitLabel(l17);
            mv.visitLineNumber(759, l17);
            mv.visitFrame(Opcodes.F_CHOP, 2, null, 0, null);
            mv.visitMethodInsn(INVOKESTATIC, "com/sun/tools/example/debug/tty/MessageOutput", "printPrompt", "()V", false);
            Label l19 = new Label();
            mv.visitLabel(l19);
            mv.visitLineNumber(761, l19);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitVarInsn(ALOAD, 1);

            // 修改了接口定义
            mv.visitMethodInsn(INVOKEINTERFACE, "org/carl/fiber/jline/JDBConsoleReader", "readLine", "()Ljava/lang/String;", true);

            mv.visitVarInsn(ASTORE, 3);
            Label l20 = new Label();
            mv.visitLabel(l20);
            mv.visitLineNumber(762, l20);
            mv.visitVarInsn(ALOAD, 3);
            Label l21 = new Label();
            mv.visitJumpInsn(IFNONNULL, l21);
            Label l22 = new Label();
            mv.visitLabel(l22);
            mv.visitLineNumber(767, l22);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/sun/tools/example/debug/tty/TTY", "isShuttingDown", "()Z", false);
            Label l23 = new Label();
            mv.visitJumpInsn(IFNE, l23);
            Label l24 = new Label();
            mv.visitLabel(l24);
            mv.visitLineNumber(768, l24);
            mv.visitLdcInsn("Input stream closed.");
            mv.visitMethodInsn(INVOKESTATIC, "com/sun/tools/example/debug/tty/MessageOutput", "println", "(Ljava/lang/String;)V", false);
            mv.visitLabel(l23);
            mv.visitLineNumber(770, l23);
            mv.visitFrame(Opcodes.F_APPEND, 1, new Object[]{"java/lang/String"}, 0, null);
            mv.visitLdcInsn("quit");
            mv.visitVarInsn(ASTORE, 3);
            mv.visitLabel(l21);
            mv.visitLineNumber(773, l21);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitLdcInsn("!!");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false);
            Label l25 = new Label();
            mv.visitJumpInsn(IFEQ, l25);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitJumpInsn(IFNULL, l25);
            Label l26 = new Label();
            mv.visitLabel(l26);
            mv.visitLineNumber(774, l26);
            mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitInsn(ICONST_2);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "substring", "(I)Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            mv.visitVarInsn(ASTORE, 3);
            Label l27 = new Label();
            mv.visitLabel(l27);
            mv.visitLineNumber(775, l27);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitMethodInsn(INVOKESTATIC, "com/sun/tools/example/debug/tty/MessageOutput", "printDirectln", "(Ljava/lang/String;)V", false);
            mv.visitLabel(l25);
            mv.visitLineNumber(778, l25);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitTypeInsn(NEW, "java/util/StringTokenizer");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitMethodInsn(INVOKESPECIAL, "java/util/StringTokenizer", "<init>", "(Ljava/lang/String;)V", false);
            mv.visitVarInsn(ASTORE, 4);
            Label l28 = new Label();
            mv.visitLabel(l28);
            mv.visitLineNumber(779, l28);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/StringTokenizer", "hasMoreTokens", "()Z", false);
            Label l29 = new Label();
            mv.visitJumpInsn(IFEQ, l29);
            Label l30 = new Label();
            mv.visitLabel(l30);
            mv.visitLineNumber(780, l30);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ASTORE, 2);
            Label l31 = new Label();
            mv.visitLabel(l31);
            mv.visitLineNumber(781, l31);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/sun/tools/example/debug/tty/TTY", "executeCommand", "(Ljava/util/StringTokenizer;)V", false);
            Label l32 = new Label();
            mv.visitJumpInsn(GOTO, l32);
            mv.visitLabel(l29);
            mv.visitLineNumber(783, l29);
            mv.visitFrame(Opcodes.F_APPEND, 1, new Object[]{"java/util/StringTokenizer"}, 0, null);
            mv.visitMethodInsn(INVOKESTATIC, "com/sun/tools/example/debug/tty/MessageOutput", "printPrompt", "()V", false);
            mv.visitLabel(l32);
            mv.visitLineNumber(785, l32);
            mv.visitFrame(Opcodes.F_CHOP, 2, null, 0, null);
            mv.visitJumpInsn(GOTO, l19);
            mv.visitLabel(l1);
            mv.visitLineNumber(786, l1);
            mv.visitFrame(Opcodes.F_FULL, 1, new Object[]{"com/sun/tools/example/debug/tty/TTY"}, 1, new Object[]{"com/sun/jdi/VMDisconnectedException"});
            mv.visitVarInsn(ASTORE, 1);
            Label l33 = new Label();
            mv.visitLabel(l33);
            mv.visitLineNumber(787, l33);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, "com/sun/tools/example/debug/tty/TTY", "handler", "Lcom/sun/tools/example/debug/tty/EventHandler;");
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/sun/tools/example/debug/tty/EventHandler", "handleDisconnectedException", "()V", false);
            Label l34 = new Label();
            mv.visitLabel(l34);
            mv.visitLineNumber(789, l34);
            mv.visitInsn(RETURN);
            mv.visitMaxs(5, 6);
            mv.visitEnd();
            return null;
        }
        return mv;
    }
}
