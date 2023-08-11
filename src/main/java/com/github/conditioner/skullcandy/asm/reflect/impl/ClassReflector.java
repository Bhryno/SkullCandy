package com.github.conditioner.skullcandy.asm.reflect.impl;

import com.github.conditioner.skullcandy.asm.reflect.AbstractReflector;

public class ClassReflector extends AbstractReflector<String> {
    private final String name;

    public ClassReflector(String name) {
        this.name = name;
    }

    @Override
    public boolean isMatch(String s) {
        return s.equals(name);
    }
}
