package com.github.tofpu.speedbridge2.bridge.core;

import com.github.tofpu.speedbridge2.bridge.core.state.StartGameState;
import com.github.tofpu.speedbridge2.bridge.core.state.StopGameState;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

public abstract class GameHandler<P extends OnlinePlayer, H extends GameHandler<P, H, G>, G extends Game<H, G>> {
    private final Map<UUID, Game<H, G>> ongoingGameMap = new HashMap<>();

    protected void assertPlayerIsNotInGame(P p) {
        requireState(!isInGame(p.id()), "%s is already in a game!", p.id());
    }

    protected void internalStart(final P p, final Game<H, G> game) {
        assertPlayerIsNotInGame(p);

        Game.GameState<H, G> gameState = createPrepareState();
        if (gameState == null) {
            gameState = createStartState();
        }
        requireState(gameState != null, "Prepare or start game state must be provided for the game to be fully functional.");
        game.dispatch(gameState);

        this.ongoingGameMap.put(p.id(), game);
    }

    public boolean isInGame(final UUID playerId) {
        return get(playerId) != null;
    }

    public Game<H, G> get(final UUID playerId) {
        Game<H, G> game = this.ongoingGameMap.get(playerId);
        if (game == null) {
            return null;
        }

        // todo: log this as automatic game correction; as this is abnormal behavior
        if (game.gameState() instanceof StopGameState) {
            this.ongoingGameMap.remove(playerId);
            return null;
        }

        return game;
    }

    @NotNull
    public Game<H, G> getSafe(final UUID playerId) {
        Game<H, G> game = get(playerId);
        if (game == null) {
            throw new RuntimeException("Player %s is not in a game!");
        }
        return game;
    }

    public void stop(final UUID playerId) {
        requireState(isInGame(playerId), "%s is not in a game!");

        Game<H, G> game = get(playerId);

//        requireState(!(game.gameState() instanceof StopGameState), "");
//        assert !(game.gameState() instanceof StopGameState);
        StopGameState<H, G> stopState = createStopState();
        requireState(stopState != null, "StopGameState implementation must be provided on GameHandler!");
        game.dispatch(stopState);

        ongoingGameMap.remove(playerId);
    }

//    public abstract Game createGame(GamePlayer gamePlayer);
//    public abstract GamePlayer createGamePlayer(P player);
    protected abstract Game.GameState<H, G> createPrepareState();
    protected abstract StartGameState<H, G> createStartState();
    protected abstract StopGameState<H, G> createStopState();
}
