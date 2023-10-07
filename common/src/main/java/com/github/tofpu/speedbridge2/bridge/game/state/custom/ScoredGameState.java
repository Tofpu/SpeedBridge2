package com.github.tofpu.speedbridge2.bridge.game.state.custom;

import com.github.tofpu.speedbridge2.bridge.core.state.StartGameState;
import com.github.tofpu.speedbridge2.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.bridge.game.IslandGameHandler;
import com.github.tofpu.speedbridge2.bridge.game.IslandGamePlayer;
import com.github.tofpu.speedbridge2.bridge.game.event.PlayerScoredEvent;
import com.github.tofpu.speedbridge2.bridge.game.state.generic.BridgeGameState;
import com.github.tofpu.speedbridge2.bridge.score.BridgeScoreService;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;

import java.util.IllegalFormatException;

class ScoredGameState implements BridgeGameState {
    private final EventDispatcherService eventDispatcherService;
    private final BridgeScoreService scoreService;

    public ScoredGameState(EventDispatcherService eventDispatcherService, BridgeScoreService scoreService) {
        this.eventDispatcherService = eventDispatcherService;
        this.scoreService = scoreService;
    }

    @Override
    public void apply(IslandGameHandler handler, IslandGame game) {
        IslandGamePlayer player = game.player();

        PlayerScoredEvent event = new PlayerScoredEvent(player, game, game.timerInSeconds());
        eventDispatcherService.dispatchIfApplicable(event);

        if (event.cancelled()) {
            return;
        }

        String finalMessage = event.scoreMessage();
        try {
            finalMessage = String.format(event.scoreMessage(), event.scoreInSeconds());
        } catch (IllegalFormatException ignored) {
        }

        scoreService.addScore(player.id(), game.getIsland().getSlot(), event.scoreInSeconds());

        player.getPlayer().sendMessage(finalMessage);
        handler.resetGame(player.id());
    }

    @Override
    public boolean test(IslandGame game) {
        return game.gameState() instanceof StartGameState;
    }
}
