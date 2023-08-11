package com.github.conditioner.skullcandy.reflect.access;

import java.lang.reflect.Field;

public class FieldAccess {
    private final Field field;

    public FieldAccess(Field field) {
        (this.field = field).setAccessible(true);
    }

    public Object get(Object instance) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void set(Object instance, Object value) {
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
