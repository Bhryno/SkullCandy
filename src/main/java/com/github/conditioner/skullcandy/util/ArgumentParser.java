package com.github.conditioner.skullcandy.util;

import java.lang.reflect.InvocationTargetException;

public class ArgumentParser {
    public static <T> T requireType(String args, Class<T> clazz) {
        try {
            return clazz.getConstructor(String.class).newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("String " + args + "could not be converted to " + clazz.getName());
        }
    }
}
