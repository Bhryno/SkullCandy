package com.github.conditioner.skullcandy.reflect.bridge;

import org.apache.commons.lang3.Validate;

public abstract class BridgeFactory {
    public abstract void init(BridgeManager bridgeManager);

    public void registerBridge(Class<?> bridge) {
        Validate.notNull(bridge, "The bridge must be not be null.");
        BridgePipeline.registerBridge(bridge);
    }
}
