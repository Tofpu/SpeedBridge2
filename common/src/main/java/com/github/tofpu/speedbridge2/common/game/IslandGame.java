package com.github.tofpu.speedbridge2.common.game;

import io.github.tofpu.speedbridge.gameengine.Game;
import io.github.tofpu.speedbridge.gameengine.StateManager;
import org.jetbrains.annotations.NotNull;

public class IslandGame extends Game<IslandGameData> {
    public IslandGame(@NotNull IslandGameData gameData, @NotNull StateManager<IslandGameData> stateChangeListener) {
        super(gameData, stateChangeListener);
    }
}
