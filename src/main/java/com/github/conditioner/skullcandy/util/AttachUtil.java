package com.github.conditioner.skullcandy.util;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

public class AttachUtil {
    public static void attach(String agentPath) {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.substring(0, name.indexOf('@'));

        try {
            File agentFile = new File(agentPath);
            VirtualMachine vm = VirtualMachine.attach(pid);

            vm.loadAgent(agentFile.getAbsolutePath(), "");
            VirtualMachine.attach(vm.id());
        } catch (AttachNotSupportedException | IOException | AgentLoadException | AgentInitializationException e) {
            throw new RuntimeException(e);
        }
    }
}
