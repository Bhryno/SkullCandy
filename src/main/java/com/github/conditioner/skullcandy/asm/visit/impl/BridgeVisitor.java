package com.github.conditioner.skullcandy.asm.visit.impl;

import com.github.conditioner.skullcandy.asm.reflect.Reflect;
import com.github.conditioner.skullcandy.asm.reflect.AbstractReflector;
import com.github.conditioner.skullcandy.asm.visit.AbstractVisitor;
import com.github.conditioner.skullcandy.util.ASMUtil;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class BridgeVisitor extends AbstractVisitor {
    private static final Map<String, ClassNode> CLASS_MAP = new HashMap<>();

    private static final String ANNO_DESC = "L" + Reflect.class.getName().replace(".", "/") + ";";
    private final String visitorPkg;

    public BridgeVisitor(AbstractReflector<?> matcher, String visitorPkg) {
        super(matcher);
        this.visitorPkg = visitorPkg;
    }

    @Override
    public void visit(ClassNode cn) {
        for (MethodNode mn : cn.methods) {
            mn.name = updateMethodName(cn, mn);
            mn.desc = updateMethodDesc(mn.desc);

            for (AbstractInsnNode ain : mn.instructions.toArray()) {
                if (ain.getType() == AbstractInsnNode.FIELD_INSN) {
                    FieldInsnNode fin = (FieldInsnNode) ain;
                    fin.name = updateFieldName(fin);
                    fin.desc = updateFieldDesc(fin.desc);
                    fin.owner = updateClassName(fin.owner);
                } else if (ain.getType() == AbstractInsnNode.METHOD_INSN) {
                    MethodInsnNode min = (MethodInsnNode) ain;
                    min.name = updateMethodName(min);
                    min.desc = updateMethodDesc(min.desc);
                    min.owner = updateClassName(min.owner);
                } else if (ain.getType() == AbstractInsnNode.TYPE_INSN) {
                    TypeInsnNode tin = (TypeInsnNode) ain;
                    tin.desc = updateClassName(tin.desc);
                } else if (ain.getType() == AbstractInsnNode.INVOKE_DYNAMIC_INSN) {
                    InvokeDynamicInsnNode idin = (InvokeDynamicInsnNode) ain;
                    idin.desc = updateMethodDesc(idin.desc);
                } else if (ain.getType() == AbstractInsnNode.FRAME) {

                    mn.instructions.set(ain, new InsnNode(Opcodes.NOP));
                }
            }
        }

        for (FieldNode fn : cn.fields) {
            fn.name = updateFieldName(cn, fn);
            if (needsRenaming(fn.desc)) {
                fn.desc = updateFieldDesc(fn.desc);
            }
        }

        if (needsRenaming(cn.superName)) {
            cn.superName = updateClassName(cn.superName);
        }
    }

    private String updateFieldName(FieldInsnNode fin) {
        ClassNode fieldOwner = getClassNode(fin.owner);
        while (true) {
            assert fieldOwner != null;
            if (fieldOwner.name.equals("java/lang/Object")) break;
            FieldNode field = getField(fieldOwner, fin.name, fin.desc);
            if (field != null) {
                return getRefactoredName(field);
            }
            fieldOwner = getClassNode(fieldOwner.superName);
        }
        return fin.name;
    }

    private String updateFieldName(ClassNode fieldOwner, FieldNode fin) {
        while (true) {
            assert fieldOwner != null;
            if (fieldOwner.name.equals("java/lang/Object")) break;
            FieldNode field = getField(fieldOwner, fin.name, fin.desc);
            if (field != null) {
                return getRefactoredName(field);
            }
            fieldOwner = getClassNode(fieldOwner.superName);
        }
        return fin.name;
    }

    private String updateMethodName(MethodInsnNode min) {
        ClassNode methodOwner = getClassNode(min.owner);
        while (true) {
            assert methodOwner != null;
            if (methodOwner.name.equals("java/lang/Object")) break;
            MethodNode method = getMethod(methodOwner, min.name, min.desc);
            if (method != null) {
                String renamed = getRefactoredName(method);
                if (!renamed.equals(min.name)) {
                    return renamed;
                }
            }
            methodOwner = getClassNode(methodOwner.superName);
        }
        return min.name;
    }

    private String updateMethodName(ClassNode methodOwner, MethodNode mn) {
        while (true) {
            assert methodOwner != null;
            if (methodOwner.name.equals("java/lang/Object")) break;
            MethodNode method = getMethod(methodOwner, mn.name, mn.desc);
            if (method != null) {
                String renamed = getRefactoredName(method);
                if (!renamed.equals(mn.name)) {
                    return renamed;
                }
            }
            methodOwner = getClassNode(methodOwner.superName);
        }
        return mn.name;
    }

    private String updateClassName(String name) {
        ClassNode cn = getClassNode(name);
        if (cn != null) {
            return getRefactoredName(cn);
        }
        return name;
    }

    private String updateFieldDesc(String desc) {
        String fieldClass = Type.getType(desc).getClassName().replace(".", "/");
        if (needsRenaming(fieldClass)) {
            ClassNode fcn = getClassNode(fieldClass);
            assert fcn != null;
            desc = desc.replace(fcn.name, getRefactoredName(fcn));
        }
        return desc;
    }

    private String updateMethodDesc(String desc) {
        Type methodType = Type.getMethodType(desc);

        for (Type arg : methodType.getArgumentTypes()) {
            String argClass = arg.getClassName().replace(".", "/");
            if (needsRenaming(argClass)) {
                ClassNode acn = getClassNode(argClass);
                assert acn != null;
                desc = desc.replace(argClass, getRefactoredName(acn));
            }
        }

        String returnClass = methodType.getReturnType().getClassName().replace(".", "/");
        if (needsRenaming(returnClass)) {
            ClassNode retcn = getClassNode(returnClass);
            assert retcn != null;
            desc = desc.replace(returnClass, getRefactoredName(retcn));
        }
        return desc;
    }

    private FieldNode getField(ClassNode cn, String name, String desc) {
        for (FieldNode fn : cn.fields) {
            if (fn.name.equals(name) && fn.desc.equals(desc)) {
                return fn;
            }
        }
        return null;
    }

    private MethodNode getMethod(ClassNode cn, String name, String desc) {
        for (MethodNode mn : cn.methods) {
            if (mn.name.equals(name) && mn.desc.equals(desc)) {
                return mn;
            }
        }
        return null;
    }

    private String getRefactoredName(ClassNode cn) {
        if (cn.visibleAnnotations != null) {
            for (AnnotationNode an : cn.visibleAnnotations) {
                if (an.desc.equals(ANNO_DESC)) {
                    return an.values.get(1).toString();
                }
            }
        }
        return cn.name;
    }

    private String getRefactoredName(FieldNode fn) {
        if (fn.visibleAnnotations != null) {
            for (AnnotationNode an : fn.visibleAnnotations) {
                if (an.desc.equals(ANNO_DESC)) {
                    return an.values.get(1).toString();
                }
            }
        }
        return fn.name;
    }

    private String getRefactoredName(MethodNode mn) {
        try {
            if (mn.visibleAnnotations != null) {
                for (AnnotationNode an : mn.visibleAnnotations) {
                    if (an.desc.equals(ANNO_DESC)) {
                        return an.values.get(1).toString();
                    }
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return mn.name;
    }

    private boolean needsRenaming(String name) {
        return name.contains(visitorPkg);
    }

    private ClassNode getClassNode(String internalName) {
        if (CLASS_MAP.containsKey(internalName)) {
            return CLASS_MAP.get(internalName);
        }
        try {
            ClassReader cr = new ClassReader(internalName);
            ClassNode cn = ASMUtil.getNode(cr.b);
            CLASS_MAP.put(cn.name, cn);

            return cn;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}