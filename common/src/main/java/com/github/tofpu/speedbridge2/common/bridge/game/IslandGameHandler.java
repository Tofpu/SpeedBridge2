package com.github.tofpu.speedbridge2.common.bridge.game;

import com.github.tofpu.speedbridge2.common.PlatformArenaAdapter;
import com.github.tofpu.speedbridge2.common.PlatformGameAdapter;
import com.github.tofpu.speedbridge2.common.bridge.BridgeGameAPI;
import com.github.tofpu.speedbridge2.common.bridge.game.listener.GameListener;
import com.github.tofpu.speedbridge2.common.bridge.game.score.BridgeScoreService;
import com.github.tofpu.speedbridge2.common.bridge.game.state.basic.GamePrepareState;
import com.github.tofpu.speedbridge2.common.bridge.game.state.basic.GameStartedState;
import com.github.tofpu.speedbridge2.common.bridge.game.state.basic.GameStopState;
import com.github.tofpu.speedbridge2.common.bridge.game.state.game.IslandResetGameState;
import com.github.tofpu.speedbridge2.common.bridge.game.state.game.ScoredGameState;
import com.github.tofpu.speedbridge2.common.gameextra.GameRegistry;
import com.github.tofpu.speedbridge2.common.gameextra.land.Land;
import com.github.tofpu.speedbridge2.common.gameextra.land.LandController;
import com.github.tofpu.speedbridge2.common.gameextra.land.arena.IslandSchematic;
import com.github.tofpu.speedbridge2.common.island.Island;
import com.github.tofpu.speedbridge2.common.lobby.LobbyService;
import com.github.tofpu.speedbridge2.common.schematic.Schematic;
import com.github.tofpu.speedbridge2.common.schematic.SchematicHandler;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import com.github.tofpu.speedbridge2.service.manager.ServiceManager;
import io.github.tofpu.speedbridge.gameengine.BaseGameHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class IslandGameHandler extends BaseGameHandler<IslandGameData> {

    private final SchematicHandler schematicHandler;
    private final LandController landController;
    private final PlatformArenaAdapter arenaAdapter;

    private final GameRegistry<IslandGame> gameRegistry = new GameRegistry<>();

    public IslandGameHandler(SchematicHandler schematicHandler, LandController landController, PlatformArenaAdapter arenaAdapter) {
        this.schematicHandler = schematicHandler;
        this.landController = landController;
        this.arenaAdapter = arenaAdapter;
    }

    @Override
    public void registerStates() {
        this.stateManager.addState(BridgeStateTypes.PREPARE, new GamePrepareState());
        this.stateManager.addState(BridgeStateTypes.START, new GameStartedState());
        this.stateManager.addState(BridgeStateTypes.STOP, new GameStopState());

        this.stateManager.addState(BridgeStateTypes.SCORED, new ScoredGameState());
        this.stateManager.addState(BridgeStateTypes.RESET, new IslandResetGameState());
    }

    public void registerListener(PlatformGameAdapter gameAdapter, ServiceManager serviceManager) {
        BridgeGameAPI.instance().eventDispatcherService().register(new GameListener(gameAdapter, serviceManager.get(BridgeScoreService.class), serviceManager.get(LobbyService.class), landController));
    }

    public boolean start(final OnlinePlayer player, final Island island) {
        if (gameRegistry.isInGame(player.id())) {
            return false;
        }

        IslandGame game = prepareGameObject(player, island);
        game.dispatch(BridgeStateTypes.PREPARE);
        gameRegistry.register(player.id(), game);
        return true;
    }

    @NotNull
    private IslandGame prepareGameObject(OnlinePlayer player, Island island) {
        IslandGamePlayer gamePlayer = new IslandGamePlayer(player);

        Schematic schematic = schematicHandler.resolve(island.getSchematicName());
        IslandSchematic islandSchematic = new IslandSchematic(island.getSlot(), schematic, island.getAbsolute());

        Land land = this.landController.reserveSpot(player.id(), islandSchematic, arenaAdapter.gameWorld());
        return new IslandGame(new IslandGameData(gamePlayer, island, land), stateManager);
    }

    public boolean stop(final UUID playerId) {
        IslandGame game = getGameByPlayer(playerId);
        if (game == null) {
            return false;
        }

        game.dispatch(BridgeStateTypes.STOP);
        gameRegistry.removeByPlayer(playerId);
        return true;
    }

    public boolean isInGame(final UUID playerId) {
        return getGameByPlayer(playerId) != null;
    }

    @Nullable
    public IslandGame getGameByPlayer(UUID playerId) {
        return gameRegistry.getByPlayer(playerId);
    }

    public LandController landController() {
        return landController;
    }
}
