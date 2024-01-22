package com.github.tofpu.speedbridge2.common.bridge;

import com.github.tofpu.speedbridge2.event.Event;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import org.jetbrains.annotations.NotNull;

public class BridgeGameAPI {
    private static BridgeGameAPI instance;
    private final @NotNull EventDispatcherService eventDispatcherService;

    public BridgeGameAPI(@NotNull EventDispatcherService eventDispatcherService) {
        this.eventDispatcherService = eventDispatcherService;
    }

    public static void setInstance(@NotNull BridgeGameAPI instance) {
        BridgeGameAPI.instance = instance;
    }

    public static void clearInstance() {
        instance = null;
    }

    public static BridgeGameAPI instance() {
        if (instance == null) {
            throw new IllegalStateException("An instance of BridgeGameAPI must be provided");
        }
        return instance;
    }

    public void dispatchEvent(Event event) {
        eventDispatcherService.dispatchIfApplicable(event);
    }

    public EventDispatcherService eventDispatcherService() {
        return eventDispatcherService;
    }
}
