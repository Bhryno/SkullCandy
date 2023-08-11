package com.github.conditioner.skullcandy;

import java.lang.instrument.Instrumentation;

public class SkullCandyRunnable implements Runnable {
    private final Instrumentation inst;

    public SkullCandyRunnable(Instrumentation inst) {
        this.inst = inst;
    }

    @Override
    public void run() {

    }
}
