package com.github.tofpu.speedbridge2.bridge.game;

import com.github.tofpu.speedbridge2.ArenaAdapter;
import com.github.tofpu.speedbridge2.GameAdapter;
import com.github.tofpu.speedbridge2.bridge.IslandSchematic;
import com.github.tofpu.speedbridge2.bridge.Land;
import com.github.tofpu.speedbridge2.bridge.LandController;
import com.github.tofpu.speedbridge2.bridge.core.Game;
import com.github.tofpu.speedbridge2.bridge.core.GameHandler;
import com.github.tofpu.speedbridge2.bridge.core.GamePlayer;
import com.github.tofpu.speedbridge2.bridge.core.state.StartGameState;
import com.github.tofpu.speedbridge2.bridge.core.state.StopGameState;
import com.github.tofpu.speedbridge2.lobby.LobbyService;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import com.github.tofpu.speedbridge2.schematic.Schematic;
import com.github.tofpu.speedbridge2.schematic.SchematicHandler;

import java.util.UUID;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

public class BridgeGameHandler extends GameHandler<OnlinePlayer> {
    private final GameAdapter gameAdapter;
    private final LobbyService lobbyService;
    private final ArenaAdapter arenaAdapter;
    private final LandController landController;
    private final SchematicHandler schematicHandler;

    public static BridgeGameHandler load(GameAdapter gameAdapter, LobbyService lobbyService, ArenaAdapter arenaAdapter, SchematicHandler schematicHandler) {
        arenaAdapter.resetAndLoadGameWorld();
        return new BridgeGameHandler(gameAdapter, lobbyService, arenaAdapter, schematicHandler);
    }

    private BridgeGameHandler(GameAdapter gameAdapter, LobbyService lobbyService, ArenaAdapter arenaAdapter, SchematicHandler schematicHandler) {
        this.gameAdapter = gameAdapter;
        this.lobbyService = lobbyService;
        this.arenaAdapter = arenaAdapter;
        this.landController = new LandController(new IslandArenaManager(arenaAdapter));
        this.schematicHandler = schematicHandler;
    }

    public void start(final OnlinePlayer player, final Island island) {
        assertPlayerIsNotInGame(player);

        GamePlayer gamePlayer = new IslandGamePlayer(player);

        Schematic schematic = schematicHandler.resolve(island.getSchematicName());
        IslandSchematic islandSchematic = new IslandSchematic(island.getSlot(), schematic, island.getAbsolute());

        Land land = this.landController.reserveSpot(player.id(), islandSchematic, arenaAdapter.gameWorld());
        Game game = new IslandGame(gamePlayer, island, land);
        internalStart(player, game);
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

    public LandController landController() {
        return landController;
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

        private final BridgeGameHandler gameHandler;

        GamePrepareState(BridgeGameHandler gameHandler) {
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
            return !(game.gameState() instanceof BridgeGameHandler.IslandResetGameState);
        }
    }

    static class GameStartedState extends StartGameState {
        @Override
        public void apply(Game game) {
            apply((IslandGame) game);
        }

        public void apply(IslandGame game) {
            if (game.gameState() instanceof BridgeGameHandler.IslandResetGameState) {
                return;
            }
            game.player().getPlayer().sendMessage("Game has begun, good luck!");
        }
    }

    static class IslandStopGameState extends StopGameState {
        private final BridgeGameHandler gameHandler;

        IslandStopGameState(BridgeGameHandler gameHandler) {
            this.gameHandler = gameHandler;
        }

        @Override
        public void apply(Game game) {
            apply((IslandGame) game);
        }

        public void apply(IslandGame game) {
            IslandGamePlayer gamePlayer = (IslandGamePlayer) game.gamePlayer();
            gamePlayer.getPlayer().teleport(gameHandler.lobbyService.position());

            gameHandler.landController.releaseSpot(gamePlayer.id());

            gameHandler.gameAdapter.cleanGame(gameHandler, game, gamePlayer);
            gamePlayer.getPlayer().sendMessage("Game ended within " + game.timerInSeconds() + " seconds, bravo!");
        }
    }

    static class IslandResetGameState implements Game.GameState {
        private final BridgeGameHandler gameHandler;

        IslandResetGameState(BridgeGameHandler gameHandler) {
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

    static class ScoredGameState implements Game.GameState {
        private final BridgeGameHandler gameHandler;

        public ScoredGameState(BridgeGameHandler gameHandler) {
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
