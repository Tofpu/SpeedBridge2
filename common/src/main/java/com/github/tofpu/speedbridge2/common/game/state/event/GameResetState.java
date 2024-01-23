package com.github.tofpu.speedbridge2.common.game.state.event;

import com.github.tofpu.speedbridge2.common.game.IslandGame;
import com.github.tofpu.speedbridge2.common.game.IslandGameData;
import com.github.tofpu.speedbridge2.common.game.IslandGamePlayer;
import com.github.tofpu.speedbridge2.common.game.event.IslandGameResetEvent;
import com.github.tofpu.speedbridge2.common.game.state.BridgeGameState;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import io.github.tofpu.speedbridge.gameengine.Game;
import io.github.tofpu.speedbridge.gameengine.GameStateType;

public class GameResetState implements BridgeGameState {
    private final EventDispatcherService eventDispatcher;

    public GameResetState(EventDispatcherService eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void onGameStateChange(Game<IslandGameData> game, GameStateType<IslandGameData> prevState, GameStateType<IslandGameData> newState) {
        IslandGamePlayer player = game.data().gamePlayer();
        IslandGameResetEvent event = new IslandGameResetEvent((IslandGame) game, player);
        eventDispatcher.dispatchIfApplicable(event);
    }
}
