# SkullCandy
An undetectable java agent that injects code seamlessly through bytecode manipulation, and bridges the gap between obfuscated and known through reflection.

---
## Tasks
+ [x] Agent
+ [x] Reflection
+ [x] Visiting
+ [x] Transforming
+ [x] Organise folder structure
+ [ ] Successful test class
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