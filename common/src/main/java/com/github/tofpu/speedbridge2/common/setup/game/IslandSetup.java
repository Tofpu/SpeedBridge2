package com.github.tofpu.speedbridge2.common.setup.game;

import io.github.tofpu.speedbridge.gameengine.Game;
import io.github.tofpu.speedbridge.gameengine.StateManager;
import org.jetbrains.annotations.NotNull;

public class IslandSetup extends Game<IslandSetupData> {
    public IslandSetup(@NotNull IslandSetupData gameData, @NotNull StateManager<IslandSetupData> stateManager) {
        super(gameData, stateManager);
    }
}
