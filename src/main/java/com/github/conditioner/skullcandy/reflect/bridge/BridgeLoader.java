package com.github.conditioner.skullcandy.reflect.bridge;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;

public class BridgeLoader {
    private final BridgeManager bridgeManager;
    private final Path extensionsPath;

    public BridgeLoader(BridgeManager bridgeManager, Path extensionsPath) {
        this.bridgeManager = bridgeManager;
        this.extensionsPath = extensionsPath;
    }

    public void registerBridges() {
        Collection<URL> urlList = new ArrayList<>();

        try (DirectoryStream<Path> jars = Files.newDirectoryStream(extensionsPath, "*.jar")) {
            for (Path jar : jars) {
                urlList.add(jar.toUri().toURL());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ClassLoader classLoader = new URLClassLoader(urlList.toArray(new URL[0]), BridgeFactory.class.getClassLoader());
        ServiceLoader<BridgeFactory> bridges = ServiceLoader.load(BridgeFactory.class, classLoader);

        for (BridgeFactory bridge : bridges) {
            bridge.init(bridgeManager);
        }
    }
}
