package com.github.conditioner.skullcandy.util;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class AttachUtil {
    public static void attach(String id, File in, String out) {
        System.setProperty("java.library.path", AttachUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1).replaceAll("/SkullCandy.jar", ""));

        try {
            Field field = ClassLoader.class.getDeclaredField("sys_paths");

            field.setAccessible(true);
            field.set(null, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        System.loadLibrary("attach");

        try {
            VirtualMachine vm = VirtualMachine.attach(id);

            vm.loadAgent(in.getAbsolutePath(), out);
            vm.detach();
        } catch (AttachNotSupportedException | IOException | AgentLoadException | AgentInitializationException e) {
            throw new RuntimeException(e);
        }
    }
}
