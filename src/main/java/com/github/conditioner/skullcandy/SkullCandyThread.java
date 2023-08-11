package com.github.conditioner.skullcandy;

import com.github.conditioner.skullcandy.reflect.asm.SkullCandyTransformer;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.InvocationTargetException;

public class SkullCandyThread  extends Thread {
    private final Instrumentation inst;

    public SkullCandyThread(Instrumentation inst) {
        super("SkullCandyStart");
        this.inst = inst;
    }

    @Override
    public void run() {
        ClassLoader classLoader = null;

        while (classLoader == null) {
            for (Class clazz : inst.getAllLoadedClasses()) {
                if (clazz.getName().equals("net.minecraft.client.main.Main")) {
                    classLoader = clazz.getClassLoader();
                    break;
                }
            }
        }

        registerVisitors();
        inst.addTransformer(new SkullCandyTransformer(), true);

        ClassLoader finalClassLoader = classLoader;

        new Thread(() -> {
            for (Class clazz : inst.getInitiatedClasses(finalClassLoader)) {
                try {
                    inst.retransformClasses(clazz);
                } catch (UnmodifiableClassException ignored) {}
            }
        }).start();

        try {
            Thread thread = new Thread((Runnable) classLoader.loadClass("com.github.conditioner.skullcandy.SkullCandyRunnable").getConstructor(Instrumentation.class).newInstance(inst), "SkullCandy-ClassLoader");

            thread.setContextClassLoader(classLoader);
            thread.start();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerVisitors() {

    }
}
