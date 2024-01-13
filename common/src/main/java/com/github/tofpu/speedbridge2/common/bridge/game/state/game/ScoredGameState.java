package com.github.tofpu.speedbridge2.common.bridge.game.state.game;

import com.github.tofpu.speedbridge2.CoreApplication;
import com.github.tofpu.speedbridge2.common.bridge.game.state.BridgeGameStateTag;
import com.github.tofpu.speedbridge2.common.game.GameStateTag;
import com.github.tofpu.speedbridge2.common.game.state.BasicGameStateTag;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGameData;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGamePlayer;
import com.github.tofpu.speedbridge2.common.bridge.game.event.PlayerScoredEvent;
import com.github.tofpu.speedbridge2.common.bridge.game.state.GameStateHandler;
import com.github.tofpu.speedbridge2.common.bridge.game.state.generic.BridgeGameState;
import com.github.tofpu.speedbridge2.common.bridge.game.score.BridgeScoreService;
import com.github.tofpu.speedbridge2.common.game.Game;
import org.jetbrains.annotations.NotNull;

import java.util.IllegalFormatException;

class ScoredGameState implements BridgeGameState {
    private final GameStateHandler stateHandler;
    private final BridgeScoreService scoreService;

    public ScoredGameState(GameStateHandler stateHandler, BridgeScoreService scoreService) {
        this.stateHandler = stateHandler;
        this.scoreService = scoreService;
    }

    @Override
    public void apply(Game<IslandGameData> game) {
        IslandGameData data = game.data();
        IslandGamePlayer player = data.gamePlayer();

        PlayerScoredEvent event = new PlayerScoredEvent(player, (IslandGame) game, data.timerInSeconds());
        CoreApplication.eventDispatcher().dispatchIfApplicable(event);

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
        return game.state().tag() == BasicGameStateTag.STARTED;
    }

    @Override
    public @NotNull GameStateTag tag() {
        return BridgeGameStateTag.SCORED;
    }
}
