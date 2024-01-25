package com.github.tofpu.speedbridge2.common.setup.state;

import com.github.tofpu.speedbridge2.common.gameextra.state.StartGameState;
import com.github.tofpu.speedbridge2.common.setup.IslandSetup;
import com.github.tofpu.speedbridge2.common.setup.IslandSetupData;
import com.github.tofpu.speedbridge2.common.setup.IslandSetupPlayer;
import com.github.tofpu.speedbridge2.common.setup.event.StartIslandSetupEvent;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import io.github.tofpu.speedbridge.gameengine.Game;
import io.github.tofpu.speedbridge.gameengine.GameStateType;

public class StartSetupState extends StartGameState<IslandSetupData> {
    private final EventDispatcherService eventDispatcher;

    public StartSetupState(EventDispatcherService eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void onGameStateChange(Game<IslandSetupData> game, GameStateType<IslandSetupData> prevState, GameStateType<IslandSetupData> newState) {
        StartIslandSetupEvent event = new StartIslandSetupEvent((IslandSetup) game);
        eventDispatcher.dispatchIfApplicable(event);
    }
}