package com.github.tofpu.speedbridge2.bridge.game.state;

import com.github.tofpu.speedbridge2.bridge.game.state.generic.BridgeGameState;
import com.github.tofpu.speedbridge2.bridge.game.state.generic.BridgeStartedState;
import com.github.tofpu.speedbridge2.bridge.game.state.generic.BridgeStopState;

public interface BasicStateProvider {
    BridgeGameState prepareState();
    BridgeStartedState startedState();
    BridgeStopState stopState();
}
