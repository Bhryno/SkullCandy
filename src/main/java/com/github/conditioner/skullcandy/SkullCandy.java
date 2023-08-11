package com.github.conditioner.skullcandy;

import java.lang.instrument.Instrumentation;

public class SkullCandy {

    private static final SkullCandy INSTANCE = new SkullCandy();

    public void skullCandyInit(Instrumentation inst) {
        new SkullCandyThread(inst).start();
    }

    public static SkullCandy getInstance() {
        return INSTANCE;
    }
}
