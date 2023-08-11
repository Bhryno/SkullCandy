package com.github.conditioner.skullcandy.asm.reflect;

import com.github.conditioner.skullcandy.asm.visit.AbstractVisitor;
import org.objectweb.asm.tree.ClassNode;

import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractReflector<T> {
    private final List<AbstractVisitor> visitors = new ArrayList<>();

    private ClassLoader loader;
    private Class<?> clazz;
    private ProtectionDomain domain;
    private byte[] buffer;

    public void update(ClassLoader loader, Class<?> clazz, ProtectionDomain domain, byte[] buffer) {
        this.loader = loader;
        this.clazz = clazz;
        this.domain = domain;
        this.buffer = buffer;
    }

    public abstract boolean isMatch(T t);

    public void visit(ClassNode cn) {
        for (AbstractVisitor visitor : visitors) {
            visitor.visit(cn);
        }
    }

    public void registerVisitors(AbstractVisitor... visitor) {
        visitors.addAll(Arrays.asList(visitor));
    }

    public List<AbstractVisitor> getVisitors() {
        return visitors;
    }

    public ClassLoader getLoader() {
        return loader;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public ProtectionDomain getDomain() {
        return domain;
    }

    public byte[] getBuffer() {
        return buffer;
    }
}
