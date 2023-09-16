package com.github.tofpu.speedbridge2.game.core;

import com.github.tofpu.speedbridge2.game.core.state.StartGameState;
import com.github.tofpu.speedbridge2.game.core.state.StopGameState;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

public abstract class GameHandler<P extends OnlinePlayer> {
    private final Map<UUID, Game> ongoingGameMap = new HashMap<>();

    protected void internalStart(final UUID playerId, final Game game) {
        requireState(!isInGame(playerId), "%s is already in a game!");

        Game.GameState startState = createStartState();
        requireState(startState != null, "StartGameState implementation must be provided on GameHandler!");
        game.dispatch(startState);

        this.ongoingGameMap.put(playerId, game);
    }

    public boolean isInGame(final UUID playerId) {
        return get(playerId) != null;
    }

    public Game get(final UUID playerId) {
        Game game = this.ongoingGameMap.get(playerId);
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
    public Game getSafe(final UUID playerId) {
        Game game = get(playerId);
        if (game == null) {
            throw new RuntimeException("Player %s is not in a game!");
        }
        return game;
    }

    public void stop(final UUID playerId) {
        requireState(isInGame(playerId), "%s is not in a game!");

        Game game = get(playerId);

//        requireState(!(game.gameState() instanceof StopGameState), "");
//        assert !(game.gameState() instanceof StopGameState);
        StopGameState stopState = createStopState();
        requireState(stopState != null, "StopGameState implementation must be provided on GameHandler!");
        game.dispatch(stopState);

        ongoingGameMap.remove(playerId);
    }

//    public abstract Game createGame(GamePlayer gamePlayer);
//    public abstract GamePlayer createGamePlayer(P player);
    protected abstract StartGameState createStartState();
    protected abstract StopGameState createStopState();
}
