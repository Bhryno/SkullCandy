package com.github.conditioner.skullcandy.reflect.access;

public class ClassAccess {
    private final Class<?> clazz;

    public ClassAccess(Class<?> clazz) {
        this.clazz = clazz;
    }

    public static ClassAccess getAccess(String name) {
        ClassAccess classAccess = null;

        try {
            classAccess = new ClassAccess(Class.forName(name));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return classAccess;
    }

    public ConstructorAccess getConstructor(Class<?>... params) {
        try {
            return new ConstructorAccess(clazz.getConstructor(params));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public MethodAccess getMethod(String name, Class<?>... params) {
        try {
            return new MethodAccess(clazz.getDeclaredMethod(name, params));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public FieldAccess getField(String name) {
        try {
            return new FieldAccess(clazz.getDeclaredField(name));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
