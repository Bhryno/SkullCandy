package com.github.conditioner.skullcandy.asm.visit.impl;

import com.github.conditioner.skullcandy.asm.reflect.AbstractReflector;
import com.github.conditioner.skullcandy.asm.visit.AbstractVisitor;
import com.github.conditioner.skullcandy.util.AccessHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class AccessVisitor extends AbstractVisitor implements Opcodes {
    public AccessVisitor(AbstractReflector<?> reflector) {
        super(reflector);
    }

    @Override
    public void visit(ClassNode cn) {
        cn.access = getAccessCode(cn.access);

        for (FieldNode fn : cn.fields) {
            fn.access = getAccessCode(fn.access);
        }
        for (MethodNode mn : cn.methods) {
            if (mn.name.contains("<")) {
                continue;
            }
            mn.access = getAccessCode(mn.access);
        }
    }

    private int getAccessCode(int access) {
        int defaultAccess = Opcodes.ACC_PUBLIC;

        if (AccessHelper.isAbstract(access)) {
            defaultAccess |= Opcodes.ACC_ABSTRACT;
        }
        if (AccessHelper.isStatic(access)) {
            defaultAccess |= Opcodes.ACC_STATIC;
        }
        if (AccessHelper.isEnum(access)) {
            defaultAccess |= Opcodes.ACC_ENUM;
        }
        if (AccessHelper.isAnnotation(access)) {
            defaultAccess |= Opcodes.ACC_ANNOTATION;
        }
        if (AccessHelper.isInterface(access)) {
            defaultAccess |= Opcodes.ACC_INTERFACE;
        }
        if (AccessHelper.isVolatile(access)) {
            defaultAccess |= Opcodes.ACC_VOLATILE;
        }
        if (AccessHelper.isVolatile(access)) {
            defaultAccess |= Opcodes.ACC_TRANSIENT;
        }
        if (AccessHelper.isSynchronized(access)) {
            defaultAccess |= Opcodes.ACC_SYNCHRONIZED;
        }
        if (AccessHelper.isNative(access)) {
            defaultAccess |= Opcodes.ACC_NATIVE;
        }
        return defaultAccess;
    }
}
