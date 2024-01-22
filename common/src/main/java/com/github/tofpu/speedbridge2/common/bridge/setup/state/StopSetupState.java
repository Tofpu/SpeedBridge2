package com.github.tofpu.speedbridge2.common.bridge.setup.state;

import com.github.tofpu.speedbridge2.common.bridge.BridgeGameAPI;
import com.github.tofpu.speedbridge2.common.bridge.setup.IslandSetup;
import com.github.tofpu.speedbridge2.common.bridge.setup.IslandSetupData;
import com.github.tofpu.speedbridge2.common.bridge.setup.event.StopIslandSetupEvent;
import com.github.tofpu.speedbridge2.common.gameextra.state.StopGameState;
import io.github.tofpu.speedbridge.gameengine.Game;
import io.github.tofpu.speedbridge.gameengine.GameStateType;

public class StopSetupState extends StopGameState<IslandSetupData> {
    @Override
    public void onGameStateChange(Game<IslandSetupData> game, GameStateType<IslandSetupData> stateChange) {
        BridgeGameAPI.instance().dispatchEvent(new StopIslandSetupEvent((IslandSetup) game));
    }
}
