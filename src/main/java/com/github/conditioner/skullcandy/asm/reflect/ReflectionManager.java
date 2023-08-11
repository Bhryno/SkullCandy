package com.github.conditioner.skullcandy.asm.reflect;

import com.github.conditioner.skullcandy.asm.visit.AbstractVisitor;
import com.github.conditioner.skullcandy.util.ASMUtil;
import com.github.conditioner.skullcandy.util.AccessHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

public class ReflectionManager implements Opcodes {
    public static AbstractVisitor visitMethodStart(AbstractReflector<String> reflector, String name, String desc, String owner, String obfuscatedName, String obfuscatedDesc) {
        return new AbstractVisitor(reflector) {
            @Override
            public void visit(ClassNode cn) {
                for (MethodNode mn : cn.methods) {
                    if (mn.name.equals(name) && mn.desc.equals(desc)) {
                        MethodInsnNode min = new MethodInsnNode(INVOKESTATIC, owner, obfuscatedName, obfuscatedDesc);
                        mn.instructions.insert(min);

                        Type obfuscatedType = Type.getMethodType(obfuscatedDesc);
                        Type type = Type.getType(desc);
                        Type obfuscatedReturnType = obfuscatedType.getReturnType();

                        int overrideIndex = -1;

                        for (int paramIndex = 0; paramIndex < type.getArgumentTypes().length; paramIndex++) {
                            Type paramType = type.getArgumentTypes()[paramIndex];

                            int varOffset = AccessHelper.isStatic(mn.access) ? 0 : 1;
                            int varIndex = paramIndex + varOffset;
                            int paramSort = paramType.getSort();

                            if (obfuscatedReturnType == paramType) {
                                overrideIndex = paramIndex;
                            }

                            ASMUtil.insertVarInstructions(mn.instructions, true, paramSort, varIndex);
                        }

                        if (overrideIndex != -1) {
                            ASMUtil.insertVarInstructions(mn.instructions, false, obfuscatedReturnType.getSort(), overrideIndex);
                        }
                    }
                }
            }
        };
    }

    public static AbstractVisitor visitMethodEnd(AbstractReflector<String> reflector, String name, String desc, String owner, String obfuscatedName, String obfuscatedDesc) {
        return new AbstractVisitor(reflector) {
            @Override
            public void visit(ClassNode cn) {
                for (MethodNode mn : cn.methods) {
                    if (mn.name.equals(name) && mn.desc.equals(desc)) {
                        MethodInsnNode min = new MethodInsnNode(INVOKESTATIC, owner, obfuscatedName, obfuscatedDesc);
                        mn.instructions.insert(min);

                        Type obfuscatedType = Type.getMethodType(obfuscatedDesc);
                        Type type = Type.getType(desc);
                        Type obfuscatedReturnType = obfuscatedType.getReturnType();

                        boolean obfuscatedHasReturn = obfuscatedReturnType != Type.VOID_TYPE;

                        if (obfuscatedHasReturn && obfuscatedReturnType != type.getReturnType()) {
                            throw new RuntimeException();
                        }

                        AbstractInsnNode ain = mn.instructions.getLast();

                        while (ain.getPrevious() != null) {
                            if (ain.getOpcode() >= IRETURN && ain.getOpcode() <= RETURN) {
                                break;
                            }
                            ain = ain.getPrevious();
                        }

                        for (int paramIndex = 0; paramIndex < type.getArgumentTypes().length; paramIndex++) {
                            int varOffset = AccessHelper.isStatic(mn.access) ? 0 : 1;
                            int varIndex = paramIndex + varOffset;

                            ASMUtil.addVarInstructions(mn.instructions, true, type.getSort(), varIndex);
                        }
                                                mn.instructions.add(new MethodInsnNode(INVOKESTATIC, owner, obfuscatedName, obfuscatedDesc, false));
                    }
                }
            }
        };
    }
}
