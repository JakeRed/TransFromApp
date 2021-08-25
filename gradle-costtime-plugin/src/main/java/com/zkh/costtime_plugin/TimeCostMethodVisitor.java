package com.zkh.costtime_plugin;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;

class TimeCostMethodVisitor extends LocalVariablesSorter implements Opcodes {

    //局部变量
    int startTime, endTime, costTime, thisMethodStack;

    public TimeCostMethodVisitor(MethodVisitor methodVisitor, int access, String desc) {
        super(ASM7, access, desc, methodVisitor);
    }

    @Override
    public void visitCode() {
        super.visitCode();
        //long startTime = System.currentTimeMillis();
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
        startTime = newLocal(Type.LONG_TYPE);
        mv.visitVarInsn(LSTORE, startTime);
    }

    @Override
    public void visitInsn(int opcode) {
        if(opcode >= IRETURN && opcode <= RETURN){
            //long endTime = System.currentTimeMillis();
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            endTime = newLocal(Type.LONG_TYPE);
            mv.visitVarInsn(LSTORE, endTime);

            //long costTime = endTime - startTime;
            mv.visitVarInsn(LLOAD, endTime);
            mv.visitVarInsn(LLOAD, startTime);
            mv.visitInsn(LSUB);
            costTime = newLocal(Type.LONG_TYPE);
            mv.visitVarInsn(LSTORE, costTime);

            //StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[0]
            mv.visitTypeInsn(NEW, "java/lang/Exception");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Exception", "<init>", "()V", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "getStackTrace", "()[Ljava/lang/StackTraceElement;", false);
            mv.visitInsn(ICONST_0);
            mv.visitInsn(AALOAD);
            thisMethodStack = newLocal(Type.getType(StackTraceElement.class));
            mv.visitVarInsn(ASTORE, thisMethodStack);

            //Log.e("zkh", String.format（"%s.%s(%s:%s)方法耗时 %d ms", thisMethodStack.getClassName(), thisMethodStack.getMethodName(),thisMethodStack.getLineNumber(),costTime));
            mv.visitLdcInsn("zkh");
            mv.visitLdcInsn(" %s.%s(%s)\u65b9\u6cd5\u8017\u65f6 %d ms");
            mv.visitInsn(ICONST_5);
            mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_0);
            mv.visitVarInsn(ALOAD, thisMethodStack);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StackTraceElement", "getClassName", "()Ljava/lang/String;", false);
            mv.visitInsn(AASTORE);
            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_1);
            mv.visitVarInsn(ALOAD, thisMethodStack);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StackTraceElement", "getMethodName", "()Ljava/lang/String;", false);
            mv.visitInsn(AASTORE);
            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_2);
            mv.visitVarInsn(ALOAD, thisMethodStack);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StackTraceElement", "getLineNumber", "()I", false);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
            mv.visitInsn(AASTORE);
            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_3);
            mv.visitVarInsn(LLOAD, costTime);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
            mv.visitInsn(AASTORE);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/String", "format", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false);
            mv.visitInsn(POP);
        }
        super.visitInsn(opcode);
    }
}
