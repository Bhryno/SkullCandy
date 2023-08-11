package com.github.conditioner.skullcandy.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.VarInsnNode;

public class ASMUtil implements Opcodes {
    public static ClassNode getNode(byte[] buffer) {
        ClassReader cr = new ClassReader(buffer);
        ClassNode cn = new ClassNode();

        try {
            cr.accept(cn, ClassReader.EXPAND_FRAMES);
        } catch (Exception e) {
            try {
                cr.accept(cn, ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG);
            } catch (Exception ignored) {}
        }
        return cn;
    }

    public static byte[] getNodeBuffer(ClassNode cn, boolean useMax) {
        ClassWriter cw = new ClassWriter(useMax ? ClassWriter.COMPUTE_MAXS : ClassWriter.COMPUTE_FRAMES);
        byte[] buffer = cw.toByteArray();

        cn.accept(cw);
        return buffer;
    }

    public static void insertVarInstructions(InsnList insnList, boolean loading, int sort, int index) {
        switch (sort) {
            case Type.ARRAY:
            case Type.OBJECT:
                insnList.insert(new VarInsnNode(loading ? ALOAD : ASTORE, index));
                break;
            case Type.INT:
            case Type.BOOLEAN:
                insnList.insert(new VarInsnNode(loading ? ILOAD : ISTORE, index));
                break;
            case Type.FLOAT:
                insnList.insert(new VarInsnNode(loading ? FLOAD : FSTORE, index));
                break;
            case Type.DOUBLE:
                insnList.insert(new VarInsnNode(loading ? DLOAD : DSTORE, index));
                break;
            case Type.LONG:
                insnList.insert(new VarInsnNode(loading ? LLOAD : LSTORE, index));
                break;
        }
    }

    public static void addVarInstructions(InsnList insnList, boolean loading, int sort, int index) {
        switch (sort) {
            case Type.ARRAY:
            case Type.OBJECT:
                insnList.add(new VarInsnNode(loading ? Opcodes.ALOAD : Opcodes.ASTORE, index));
                break;
            case Type.INT:
            case Type.BOOLEAN:
                insnList.add(new VarInsnNode(loading ? Opcodes.ILOAD : Opcodes.ISTORE, index));
                break;
            case Type.FLOAT:
                insnList.add(new VarInsnNode(loading ? Opcodes.FLOAD : Opcodes.FSTORE, index));
                break;
            case Type.DOUBLE:
                insnList.add(new VarInsnNode(loading ? Opcodes.DLOAD : Opcodes.DSTORE, index));
                break;
            case Type.LONG:
                insnList.add(new VarInsnNode(loading ? Opcodes.LLOAD : Opcodes.LSTORE, index));
                break;
        }
    }
}
