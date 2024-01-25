package com.github.tofpu.speedbridge2.common.setup.event;

import com.github.tofpu.speedbridge2.common.setup.IslandSetup;
import com.github.tofpu.speedbridge2.event.Cancellable;
import com.github.tofpu.speedbridge2.event.Event;

public class IslandSetupEvent extends Event implements Cancellable {
    private final IslandSetup islandSetup;
    private boolean cancelled = false;

    public IslandSetupEvent(IslandSetup islandSetup) {
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
