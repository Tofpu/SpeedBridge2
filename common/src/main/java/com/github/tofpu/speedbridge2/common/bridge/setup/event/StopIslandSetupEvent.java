package com.github.tofpu.speedbridge2.common.bridge.setup.event;

import com.github.tofpu.speedbridge2.common.bridge.setup.IslandSetup;
import com.github.tofpu.speedbridge2.event.Cancellable;
import com.github.tofpu.speedbridge2.event.Event;

public class StopIslandSetupEvent extends Event implements Cancellable {
    private final IslandSetup islandSetup;
    private boolean cancelled = false;

    public StopIslandSetupEvent(IslandSetup islandSetup) {
        this.islandSetup = islandSetup;
    }

    public IslandSetup islandSetup() {
        return islandSetup;
    }

    @Override
    public void cancel(boolean state) {
        this.cancelled = state;
    }

    @Override
    public boolean cancelled() {
        return this.cancelled;
    }
}
