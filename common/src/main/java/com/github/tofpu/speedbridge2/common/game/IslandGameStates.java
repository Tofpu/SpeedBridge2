package com.github.tofpu.speedbridge2.common.game;

import io.github.tofpu.speedbridge.gameengine.GameStateType;

public class IslandGameStates {
    public static GameStateType<IslandGameData> PREPARE = (game) -> game.stateType() != IslandGameStates.STOP;
    public static GameStateType<IslandGameData> START = (game) -> game.stateType() != IslandGameStates.STOP;
    public static GameStateType<IslandGameData> STOP = (game) -> game.stateType() != IslandGameStates.STOP;

    public static GameStateType<IslandGameData> SCORED = (game) -> game.stateType() == START;
    public static GameStateType<IslandGameData> RESET = (game) -> game.stateType() != STOP;
}
