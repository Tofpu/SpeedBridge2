package com.github.tofpu.speedbridge2.common.bridge.setup.state;

import com.github.tofpu.speedbridge2.common.bridge.setup.IslandSetup;
import com.github.tofpu.speedbridge2.common.bridge.setup.IslandSetupData;
import com.github.tofpu.speedbridge2.common.bridge.setup.event.StopIslandSetupEvent;
import com.github.tofpu.speedbridge2.common.gameextra.state.StopGameState;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import io.github.tofpu.speedbridge.gameengine.Game;
import io.github.tofpu.speedbridge.gameengine.GameStateType;

public class StopSetupState extends StopGameState<IslandSetupData> {
    private final EventDispatcherService eventDispatcher;

    public StopSetupState(EventDispatcherService eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void onGameStateChange(Game<IslandSetupData> game, GameStateType<IslandSetupData> stateChange) {
        eventDispatcher.dispatchIfApplicable(new StopIslandSetupEvent((IslandSetup) game));
    }
}
