package com.github.conditioner.skullcandy.asm.reflect.impl;

import com.github.conditioner.skullcandy.asm.reflect.AbstractReflector;

public class DefaultPackageReflector extends AbstractReflector<String> {
    @Override
    public boolean isMatch(String s) {
        return !s.contains("/");
    }
}
