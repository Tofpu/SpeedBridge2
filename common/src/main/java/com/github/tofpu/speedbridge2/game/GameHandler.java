package com.github.tofpu.speedbridge2.game;

import com.github.tofpu.speedbridge2.game.state.StartGameState;
import com.github.tofpu.speedbridge2.game.state.StopGameState;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

public abstract class GameHandler<P extends OnlinePlayer, D extends GameData> {
    protected final GameRegistry<Game<D>> gameRegistry = new GameRegistry<>();

    protected void assertPlayerIsNotInGame(P p) {
        requireState(!isInGame(p.id()), "%s is already in a game!", p.id());
    }

    protected void prepareAndRegister(final P p, final Game<D> game) {
        assertPlayerIsNotInGame(p);

        GameState<D> gameState = createPrepareState();
        if (gameState == null) {
            gameState = createStartState();
        }
        requireState(gameState != null, "Prepare or start game state must be provided for the game to be fully functional.");
        game.dispatch(gameState);

        gameRegistry.register(p.id(), game);
    }

    public boolean isInGame(final UUID playerId) {
        return gameRegistry.isInGame(playerId);
    }

    public Game<D> getByPlayer(final UUID playerId) {
        return gameRegistry.getByPlayer(playerId);
    }

    @NotNull
    public Game<D> getSafe(final UUID playerId) {
        Game<D> game = getByPlayer(playerId);
        if (game == null) {
            throw new IllegalStateException(String.format("Player %s is not in a game!", playerId));
        }
        return game;
    }

    public void stop(final UUID playerId) {
        requireState(isInGame(playerId), "%s is not in a game!", playerId);

        Game<D> game = getByPlayer(playerId);

        StopGameState<D> stopState = createStopState();
        requireState(stopState != null, "StopGameState implementation must be provided on GameHandler!");
        game.dispatch(stopState);

        gameRegistry.removeByPlayer(playerId);
    }

    protected abstract GameState<D> createPrepareState();
    protected abstract StartGameState<D> createStartState();
    protected abstract StopGameState<D> createStopState();
}
