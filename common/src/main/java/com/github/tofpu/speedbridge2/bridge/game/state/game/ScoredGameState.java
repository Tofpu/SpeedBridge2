package com.github.tofpu.speedbridge2.bridge.game.state.game;

import com.github.tofpu.speedbridge2.bridge.core.state.StartGameState;
import com.github.tofpu.speedbridge2.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.bridge.game.IslandGameData;
import com.github.tofpu.speedbridge2.bridge.game.IslandGamePlayer;
import com.github.tofpu.speedbridge2.bridge.game.event.PlayerScoredEvent;
import com.github.tofpu.speedbridge2.bridge.game.state.GameStateHandler;
import com.github.tofpu.speedbridge2.bridge.game.state.generic.BridgeGameState;
import com.github.tofpu.speedbridge2.bridge.score.BridgeScoreService;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.bridge.core.Game;

import java.util.IllegalFormatException;

class ScoredGameState implements BridgeGameState {
    private final GameStateHandler stateHandler;
    private final EventDispatcherService eventDispatcherService;
    private final BridgeScoreService scoreService;

    public ScoredGameState(GameStateHandler stateHandler, EventDispatcherService eventDispatcherService, BridgeScoreService scoreService) {
        this.stateHandler = stateHandler;
        this.eventDispatcherService = eventDispatcherService;
        this.scoreService = scoreService;
    }

    @Override
    public void apply(Game<IslandGameData> game) {
        IslandGameData data = game.data();
        IslandGamePlayer player = data.gamePlayer();

        PlayerScoredEvent event = new PlayerScoredEvent(player, (IslandGame) game, data.timerInSeconds());
        eventDispatcherService.dispatchIfApplicable(event);

        if (event.cancelled()) {
            return;
        }

        String finalMessage = event.scoreMessage();
        try {
            finalMessage = String.format(event.scoreMessage(), event.scoreInSeconds());
        } catch (IllegalFormatException ignored) {
        }

        scoreService.addScore(player.id(), data.getIsland().getSlot(), event.scoreInSeconds());

        player.getPlayer().sendMessage(finalMessage);
        stateHandler.triggerResetState(game);
    }

    @Override
    public boolean test(Game<IslandGameData> game) {
        return game.state() instanceof StartGameState;
    }
}
