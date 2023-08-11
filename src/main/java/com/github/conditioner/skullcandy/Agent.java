package com.github.conditioner.skullcandy;

import java.lang.instrument.Instrumentation;

public class Agent {
    public static void premain(String args, Instrumentation inst) {
        SkullCandy.getInstance().skullCandyInit(inst);
    }
}
