package com.github.conditioner.skullcandy.reflect.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.HashMap;

public class SkullCandyTransformer implements ClassFileTransformer {
    private static final HashMap<IVisitor, String> visitorMap = new HashMap<>();

    @Override
    public byte[] transform(ClassLoader loader, String name, Class<?> clazz, ProtectionDomain protectionDomain, byte[] buffer) {
        ClassReader cr = new ClassReader(buffer);
        ClassNode cn = new ClassNode();

        cr.accept(cn, 0);

        for (IVisitor visitor : visitorMap.keySet()) {
            if (visitorMap.get(visitor).equals(name)) {
                for (Method method : visitor.getClass().getDeclaredMethods()) {
                    Invoke invoke = method.getDeclaredAnnotation(Invoke.class);

                    if (invoke != null) {
                        String methodName = invoke.name();
                        String methodDesc = invoke.desc();

                        for (MethodNode mn : cn.methods) {
                            if (mn.name.equals(methodName) && mn.desc.equals(methodDesc)) {
                                try {
                                    method.invoke(null, mn);
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
            }
        }

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        cn.accept(cw);
        return cw.toByteArray();
    }

    public static void registerVisitor(IVisitor visitor) {
        String mapping = visitor.getClass().getDeclaredAnnotation(Hook.class).value();

        visitorMap.put(visitor, mapping);
    }
}
