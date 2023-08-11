package com.github.conditioner.skullcandy.reflect.bridge;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BridgePipeline {
    private static final Logger LOGGER = LogManager.getLogger(BridgePipeline.class.getSimpleName());

    private static final MultiValuedMap<String, Class<?>> BRIDGE_MAP = new ArrayListValuedHashMap<>();

    public static void registerBridge(Class<?> bridge) {
        if (!bridge.isAnnotationPresent(Bridge.class)) {
            LOGGER.error(bridge.getName() + " has no Bridge annotation.");
            return;
        }

        String mapping = bridge.getAnnotation(Bridge.class).value();

        BRIDGE_MAP.put(mapping, bridge);
    }
}
