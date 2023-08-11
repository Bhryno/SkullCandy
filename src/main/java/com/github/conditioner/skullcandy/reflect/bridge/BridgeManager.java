package com.github.conditioner.skullcandy.reflect.bridge;

import org.apache.commons.lang3.Validate;

import java.nio.file.Path;

public class BridgeManager {
    public BridgeManager(Path path) {
        Validate.notNull(path, "The path cannot be null");

        BridgeLoader bridgeLoader = new BridgeLoader(this, path);

        bridgeLoader.registerBridges();
    }
}
