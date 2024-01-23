package com.github.tofpu.speedbridge2.common.game;

import io.github.tofpu.speedbridge.gameengine.GameStateType;

public class BridgeStateTypes {
    public static GameStateType<IslandGameData> PREPARE = (game) -> game.stateType() != BridgeStateTypes.STOP;
    public static GameStateType<IslandGameData> START = (game) -> game.stateType() != BridgeStateTypes.STOP;
    public static GameStateType<IslandGameData> STOP = (game) -> game.stateType() != BridgeStateTypes.STOP;

    public static GameStateType<IslandGameData> SCORED = (game) -> game.stateType() == START;
    public static GameStateType<IslandGameData> RESET = (game) -> game.stateType() != STOP;
}
