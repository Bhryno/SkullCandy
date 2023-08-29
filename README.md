# SkullCandy
An undetectable java agent that injects code seamlessly through bytecode manipulation, and bridges the gap between obfuscated and known through reflection.

---
## Instructions
1. Minecraft can be accessed directly through `Bridge` classes, which can access any class members regardless of their modifiers. In order to access these class members, the `obfuscated` name needs to be inserted. The beauty of it is that `reflected` names don't have to match the remapped name:
```java
import com.github.conditioner.skullcandy.asm.reflect.Reflect;

@Reflect("avn")
public class FontRenderer { // net.minecraft.client.gui.FontRenderer
    @Reflect("a")
    public int fontHeight; // public int FONT_HEIGHT = 9;
    
    /* 
     * Certain class members such as the constructor (<clinit> and <init>) shouldn't be touched.
     * */
    @Reflect("a()V")
    public int getTextWidth(String text) { // public int getStringWidth(String text) {}
        return 0;
    }
}
```
2. Register classes and packages:

```java
package com.github.conditioner.skullcandy;

import com.github.conditioner.skullcandy.asm.reflect.AbstractReflector;
import com.github.conditioner.skullcandy.asm.reflect.impl.ClassReflector;
import com.github.conditioner.skullcandy.asm.reflect.impl.PackageReflector;
import com.github.conditioner.skullcandy.asm.visit.impl.BridgeVisitor;

import java.lang.instrument.Instrumentation;

public class SkullCandyAgent {
    public static void premain(String args, Instrumentation inst) {
        registerTransformer(inst);
    }

    public static void agentmain(String args, Instrumentation inst) {
        registerTransformer(inst);
    }

    private static void registerTransformer(Instrumentation inst) {
        inst.addTransformer(new ClassTransformer());
        System.out.println("Instrumentation: [Redefinition: " + inst.isRedefineClassesSupported() + " , Transformation: " + inst.isRetransformClassesSupported() + " ]");

        // Reflecting
        PackageReflector reflector = new PackageReflector("com/github/conditioner/skullcandy");

        reflector.registerVisitors(new BridgeVisitor(reflector, "startup"));
        ClassTransformer.registerReflector(reflector);

        // Visiting
        ClassReflector minecraft = new ClassReflector("ave");
        
        minecraft.registerVisitors(new MinecraftVisitor(minecraft));
        ClassTransformer.registerReflector(minecraft);
    }
}
```
3. If within a Minecraft Forge 1.8.9 development environment, `interpretation` is available by adding the VM options below OR the project can be compiler as a `JAR` file to be used in the Minecraft Launcher with the same arguments below:
```shell
-javaagent:"path/to/SkullCandy.jar" -noverify
```

## Tasks
+ [x] Agent
+ [x] Reflection
+ [x] Visiting
+ [x] Transforming
+ [x] Organise folder structure
+ [x] Documentation
+ [x] Successful test class
+ [ ] QoL enhancements
+ [ ] Settings
+ [ ] Modules

## Libraries
```xml
<dependency>
    <groupId>com.sun</groupId>
    <artifactId>tools</artifactId>
    <version>1.8</version>
    <scope>system</scope>
    <systemPath>${java.home}/../lib/tools.jar</systemPath>
</dependency>

<dependency>
    <groupId>org.ow2.asm</groupId>
    <artifactId>asm</artifactId>
    <version>9.5</version>
</dependency>

<dependency>
    <groupId>org.ow2.asm</groupId>
    <artifactId>asm-tree</artifactId>
    <version>9.5</version>
</dependency>
```
