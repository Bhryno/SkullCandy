package com.github.conditioner.skullcandy;

import com.github.conditioner.skullcandy.asm.reflect.AbstractReflector;
import com.github.conditioner.skullcandy.util.ASMUtil;
import org.objectweb.asm.tree.ClassNode;

import java.io.Serializable;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.*;

@SuppressWarnings("all")
public class ClassTransformer implements ClassFileTransformer {
    private static final Set<AbstractReflector<String>> REFLECTORS = new HashSet<>();

    @Override
    public byte[] transform(ClassLoader loader, String name, Class<?> clazz, ProtectionDomain domain, byte[] buffer) {
        ClassNode cn = ASMUtil.getNode(buffer);
        boolean visited = false;

        for (AbstractReflector<String> reflector : REFLECTORS) {
            if (reflector.isMatch(name)) {
                visited = true;
                reflector.update(loader, clazz, domain, buffer);
                reflector.visit(cn);
            }
        }

        if (visited) {
            buffer = ASMUtil.getNodeBuffer(cn, true);
        }
        return buffer;
    }

    public static void registerReflector(AbstractReflector<String> reflector) {
        REFLECTORS.add(reflector);
    }

    static {
        Serializable.class.getName();
        Cloneable.class.getName();
        Iterable.class.getName();
        Collection.class.getName();
        AbstractCollection.class.getName();
        Set.class.getName();
        AbstractSet.class.getName();
        HashSet.class.getName();
    }
}
