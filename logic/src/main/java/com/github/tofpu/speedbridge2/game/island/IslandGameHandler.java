package com.github.tofpu.speedbridge2.game.island;

import com.github.tofpu.speedbridge2.GameAdapter;
import com.github.tofpu.speedbridge2.game.core.Game;
import com.github.tofpu.speedbridge2.game.core.GameHandler;
import com.github.tofpu.speedbridge2.game.core.GamePlayer;
import com.github.tofpu.speedbridge2.game.core.arena.ArenaManager;
import com.github.tofpu.speedbridge2.game.island.arena.Land;
import com.github.tofpu.speedbridge2.game.core.state.StartGameState;
import com.github.tofpu.speedbridge2.game.core.state.StopGameState;
import com.github.tofpu.speedbridge2.lobby.LobbyService;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

public class IslandGameHandler extends GameHandler<OnlinePlayer> {
    protected final GameAdapter gameAdapter;
    protected final LobbyService lobbyService;
    protected final ArenaManager arenaManager;
    private final Map<UUID, Land> playerLandMapping = new HashMap<>();

    public IslandGameHandler(GameAdapter gameAdapter, LobbyService lobbyService, ArenaManager arenaManager) {
        this.gameAdapter = gameAdapter;
        this.lobbyService = lobbyService;
        this.arenaManager = arenaManager;
    }

    public void start(final OnlinePlayer player, final Island island) {
        requireState(!isInGame(player.id()), "%s is already in a game!");

        GamePlayer gamePlayer = createGamePlayer(player);

        Land land = this.arenaManager.generate(island);
        playerLandMapping.put(player.id(), land);

        Game game = createGame(gamePlayer, island, land);

        super.internalStart(player.id(), game);
    }

    public void resetGame(UUID playerId) {
        requireState(isInGame(playerId), "%s must already be in a game to reset it", playerId);

        Game game = get(playerId);
        game.dispatch(new IslandResetGameState(this));
    }

    public void scoredGame(UUID playerId) {
        requireState(isInGame(playerId), "%s must already be in a game to score", playerId);

        Game game = get(playerId);
        game.dispatch(new ScoredGameState(this));
    }

    @Override
    public void stop(UUID playerId) {
        super.stop(playerId);

        Land land = playerLandMapping.get(playerId);
        this.arenaManager.unlock(land);
    }

    protected Game createGame(GamePlayer gamePlayer, Island island, Land land) {
        return new IslandGame(gamePlayer, island, land);
    }

    protected GamePlayer createGamePlayer(OnlinePlayer player) {
        return new IslandGamePlayer(player);
    }

    @Override
    protected Game.GameState createPrepareState() {
        return new GamePrepareState(this);
    }

    @Override
    protected StartGameState createStartState() {
        return new GameStartedState();
    }

    @Override
    protected StopGameState createStopState() {
        return new IslandStopGameState(this);
    }

    static class GamePrepareState implements Game.GameState {

        private final IslandGameHandler gameHandler;

        GamePrepareState(IslandGameHandler gameHandler) {
            this.gameHandler = gameHandler;
        }

        @Override
        public void apply(Game game) {
            apply((IslandGame) game);
        }

        public void apply(IslandGame game) {
            IslandGamePlayer player = game.player();
            gameHandler.gameAdapter.prepareGame(gameHandler, game, player);

            game.dispatch(new GameStartedState());
        }

        @Override
        public boolean test(Game game) {
            return !(game.gameState() instanceof IslandResetGameState);
        }
    }

    static class GameStartedState extends StartGameState {
        @Override
        public void apply(Game game) {
            apply((IslandGame) game);
        }

        public void apply(IslandGame game) {
            if (game.gameState() instanceof IslandResetGameState) {
                return;
            }
            game.player().getPlayer().sendMessage("Game has begun, good luck!");
        }
    }

    static class IslandStopGameState extends StopGameState {
        private final IslandGameHandler gameHandler;

        IslandStopGameState(IslandGameHandler gameHandler) {
            this.gameHandler = gameHandler;
        }

        @Override
        public void apply(Game game) {
            apply((IslandGame) game);
        }

        public void apply(IslandGame game) {
            IslandGamePlayer gamePlayer = (IslandGamePlayer) game.gamePlayer();
            gamePlayer.getPlayer().teleport(gameHandler.lobbyService.position());

            gameHandler.gameAdapter.cleanGame(gameHandler, game, gamePlayer);
            gamePlayer.getPlayer().sendMessage("Game ended within " + game.timerInSeconds() + " seconds, bravo!");
        }
    }

    static class IslandResetGameState implements Game.GameState {
        private final IslandGameHandler gameHandler;

        IslandResetGameState(IslandGameHandler gameHandler) {
            this.gameHandler = gameHandler;
        }

        @Override
        public void apply(Game game) {
            apply((IslandGame) game);
        }

        public void apply(IslandGame game) {
            System.out.println("IslandResetGameState -- beginning");
            IslandGamePlayer player = game.player();

            System.out.println("IslandResetGameState -- resetting game");
            gameHandler.gameAdapter.resetGame(gameHandler, game, player);

            System.out.println("IslandResetGameState -- dispatching GameStartedState");
            game.dispatch(new GameStartedState());

            System.out.println("IslandResetGameState -- ending");
        }

        @Override
        public boolean test(Game game) {
            return game.gameState() instanceof StartGameState;
        }
    }

    private class ScoredGameState implements Game.GameState {
        private final IslandGameHandler gameHandler;

        public ScoredGameState(IslandGameHandler gameHandler) {
            this.gameHandler = gameHandler;
        }

        @Override
        public void apply(Game game) {
            apply((IslandGame) game);
        }

        public void apply(IslandGame game) {
            IslandGamePlayer player = game.player();
            player.getPlayer().sendMessage("Scored " + game.timerInSeconds() + " seconds!");

            gameHandler.resetGame(player.id());
        }

        @Override
        public boolean test(Game game) {
            return game.gameState() instanceof StartGameState;
        }
    }
}
