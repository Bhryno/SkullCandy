package com.github.conditioner.skullcandy.asm.reflect;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Reflect {
    String value();
}
