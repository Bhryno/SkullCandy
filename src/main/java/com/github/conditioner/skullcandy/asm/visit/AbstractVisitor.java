package com.github.conditioner.skullcandy.asm.visit;

import com.github.conditioner.skullcandy.asm.reflect.AbstractReflector;
import org.objectweb.asm.tree.ClassNode;

public abstract class AbstractVisitor {
    private final AbstractReflector<?> reflector;

    public AbstractVisitor(AbstractReflector<?> reflector) {
        this.reflector = reflector;
    }

    public abstract void visit(ClassNode cn);

    public AbstractReflector<?> getReflector() {
        return reflector;
    }
}
