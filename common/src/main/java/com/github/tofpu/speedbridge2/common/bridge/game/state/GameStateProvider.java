package com.github.tofpu.speedbridge2.common.bridge.game.state;

import com.github.tofpu.speedbridge2.common.bridge.game.state.generic.BridgeGameState;

public interface GameStateProvider {
    BridgeGameState scoreState();
    BridgeGameState resetState();
}
