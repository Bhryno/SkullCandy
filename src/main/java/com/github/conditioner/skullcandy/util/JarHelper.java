package com.github.conditioner.skullcandy.util;

import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

public class JarHelper {
    public static void appendJar(Instrumentation inst, JarFile jarFile) {
        inst.appendToSystemClassLoaderSearch(jarFile);
    }
}
