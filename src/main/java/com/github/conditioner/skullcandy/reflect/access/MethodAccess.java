package com.github.conditioner.skullcandy.reflect.access;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodAccess {
    private final Method method;

    public MethodAccess(Method method) {
        (this.method = method).setAccessible(true);
    }

    public Object invoke(Object instance, Object... args) {
        try {
            return method.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
