package com.github.tofpu.speedbridge2.common;

import com.github.tofpu.speedbridge2.common.setup.IslandSetup;
import com.github.tofpu.speedbridge2.common.setup.IslandSetupPlayer;

public interface PlatformSetupAdapter {
    void onSetupPrepare(IslandSetup islandSetup, IslandSetupPlayer player);
    void onSetupStop(IslandSetup islandSetup, IslandSetupPlayer player);
}
