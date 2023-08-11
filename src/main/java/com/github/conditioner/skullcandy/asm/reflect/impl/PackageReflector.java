package com.github.conditioner.skullcandy.asm.reflect.impl;

import com.github.conditioner.skullcandy.asm.reflect.AbstractReflector;

public class PackageReflector extends AbstractReflector<String> {
    private final String pkg;

    public PackageReflector(String pkg) {
        this.pkg = pkg;
    }

    @Override
    public boolean isMatch(String s) {
        return s.startsWith(pkg);
    }
}
