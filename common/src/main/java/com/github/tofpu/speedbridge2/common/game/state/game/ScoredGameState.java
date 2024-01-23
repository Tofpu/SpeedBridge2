package com.github.tofpu.speedbridge2.common.game.state.game;

import com.github.tofpu.speedbridge2.common.game.IslandGame;
import com.github.tofpu.speedbridge2.common.game.IslandGameData;
import com.github.tofpu.speedbridge2.common.game.IslandGamePlayer;
import com.github.tofpu.speedbridge2.common.game.event.PlayerScoredEvent;
import com.github.tofpu.speedbridge2.common.game.state.generic.BridgeGameState;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import io.github.tofpu.speedbridge.gameengine.Game;
import io.github.tofpu.speedbridge.gameengine.GameStateType;

public class ScoredGameState implements BridgeGameState {
    private final EventDispatcherService eventDispatcher;

    public ScoredGameState(EventDispatcherService eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void onGameStateChange(Game<IslandGameData> game, GameStateType<IslandGameData> stateChange) {
        IslandGameData data = game.data();
        IslandGamePlayer player = data.gamePlayer();

        PlayerScoredEvent event = new PlayerScoredEvent((IslandGame) game, player);
        eventDispatcher.dispatchIfApplicable(event);
    }
}