package com.zkh.costtime_plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TimeCostClassVisitor extends ClassVisitor implements Opcodes {

    private String mPackage;
    private String mCurClassName;
    private boolean isExcludeOtherPackage;//是否排除第三方库

    public TimeCostClassVisitor(ClassVisitor classVisitor) {
        super(ASM7, classVisitor);
        mPackage = TimeCostPlugin.sPackage;
        if(mPackage.length() > 0){
            mPackage = mPackage.replace(".", "/");
        }
        isExcludeOtherPackage = mPackage.length() > 0;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        mCurClassName = name;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        if(isExcludeOtherPackage){
            if(mCurClassName.startsWith(mPackage) && !"<init>".equals(name)){
                return new TimeCostMethodVisitor(methodVisitor, access, descriptor);
            }
        }else {
            if(!"<init>".equals(name)){
                return new TimeCostMethodVisitor(methodVisitor, access, descriptor);
            }
        }
        return methodVisitor;
    }


}
