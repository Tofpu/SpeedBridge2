package com.github.tofpu.speedbridge2.common.setup.event;

import com.github.tofpu.speedbridge2.common.setup.IslandSetup;

public class StartIslandSetupEvent extends IslandSetupEvent {
    public StartIslandSetupEvent(IslandSetup islandSetup) {
        super(islandSetup);
    }
}
