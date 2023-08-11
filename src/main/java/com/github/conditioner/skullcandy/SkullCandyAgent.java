package com.github.conditioner.skullcandy;

import java.lang.instrument.Instrumentation;

public class SkullCandyAgent {
    public static void premain(String args, Instrumentation inst) {}

    public static void agentmain(String args, Instrumentation inst) {}

    private static void registerTransformer(Instrumentation inst) {
        inst.addTransformer(new ClassTransformer());
        System.out.println("Instrumentation: [Redefinition: " + inst.isRedefineClassesSupported() + " , Transformation: " + inst.isRetransformClassesSupported() + " ]");
    }
}
