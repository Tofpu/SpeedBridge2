package com.github.tofpu.speedbridge2.bridge.game;

import com.github.tofpu.speedbridge2.ArenaAdapter;
import com.github.tofpu.speedbridge2.bridge.IslandSchematic;
import com.github.tofpu.speedbridge2.bridge.Land;
import com.github.tofpu.speedbridge2.bridge.LandController;
import com.github.tofpu.speedbridge2.bridge.core.Game;
import com.github.tofpu.speedbridge2.bridge.core.GameHandler;
import com.github.tofpu.speedbridge2.bridge.core.GamePlayer;
import com.github.tofpu.speedbridge2.bridge.core.state.StartGameState;
import com.github.tofpu.speedbridge2.bridge.core.state.StopGameState;
import com.github.tofpu.speedbridge2.bridge.game.state.CoreStateProvider;
import com.github.tofpu.speedbridge2.bridge.game.state.GameStateProvider;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import com.github.tofpu.speedbridge2.schematic.Schematic;
import com.github.tofpu.speedbridge2.schematic.SchematicHandler;

import java.util.UUID;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

@SuppressWarnings({"rawtypes", "unchecked"})
public class IslandGameHandler extends GameHandler<OnlinePlayer, IslandGameHandler, IslandGame> {
    private final CoreStateProvider coreStateProvider;
    private final GameStateProvider gameStateProvider;
    private final ArenaAdapter arenaAdapter;
    private final LandController landController;
    private final SchematicHandler schematicHandler;

    public static IslandGameHandler create(CoreStateProvider coreStateProvider, GameStateProvider stateProvider, ArenaAdapter arenaAdapter, SchematicHandler schematicHandler) {
        return new IslandGameHandler(coreStateProvider, stateProvider, arenaAdapter, schematicHandler);
    }

    private IslandGameHandler(CoreStateProvider coreStateProvider, GameStateProvider gameStateProvider, ArenaAdapter arenaAdapter, SchematicHandler schematicHandler) {
        this.coreStateProvider = coreStateProvider;
        this.gameStateProvider = gameStateProvider;
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
        Game<IslandGameHandler, IslandGame> game = new IslandGame(this, gamePlayer, island, land);
        internalStart(player, game);
    }

    public void resetGame(UUID playerId) {
        requireState(isInGame(playerId), "%s must already be in a game to reset it", playerId);

        Game game = get(playerId);
        game.dispatch(gameStateProvider.resetState());
    }

    public void scoredGame(UUID playerId) {
        requireState(isInGame(playerId), "%s must already be in a game to score", playerId);

        Game game = get(playerId);
        game.dispatch(gameStateProvider.scoreState());
    }

    public LandController landController() {
        return landController;
    }

    @Override
    protected Game.GameState createPrepareState() {
        return coreStateProvider.prepareState();
    }

    @Override
    protected StartGameState createStartState() {
        return coreStateProvider.startedState();
    }

    @Override
    protected StopGameState createStopState() {
        return coreStateProvider.stopState();
    }

    public CoreStateProvider coreStateProvider() {
        return coreStateProvider;
    }
}
