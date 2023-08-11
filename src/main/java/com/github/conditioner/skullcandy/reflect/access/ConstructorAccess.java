package com.github.conditioner.skullcandy.reflect.access;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorAccess {
    private final Constructor constructor;

    public ConstructorAccess(Constructor constructor) {
        (this.constructor = constructor).setAccessible(true);
    }

    public Object newInstance(Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
