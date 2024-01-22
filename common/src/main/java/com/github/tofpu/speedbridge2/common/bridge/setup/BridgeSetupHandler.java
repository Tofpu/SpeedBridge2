package com.github.tofpu.speedbridge2.common.bridge.setup;

import com.github.tofpu.speedbridge2.common.PlatformArenaAdapter;
import com.github.tofpu.speedbridge2.common.bridge.setup.state.SetOriginState;
import com.github.tofpu.speedbridge2.common.bridge.setup.state.StartSetupState;
import com.github.tofpu.speedbridge2.common.bridge.setup.state.StopSetupState;
import com.github.tofpu.speedbridge2.common.gameextra.GameRegistry;
import com.github.tofpu.speedbridge2.common.gameextra.land.Land;
import com.github.tofpu.speedbridge2.common.gameextra.land.LandController;
import com.github.tofpu.speedbridge2.common.gameextra.land.arena.IslandSchematic;
import com.github.tofpu.speedbridge2.common.island.Island;
import com.github.tofpu.speedbridge2.common.island.IslandService;
import com.github.tofpu.speedbridge2.common.schematic.Schematic;
import com.github.tofpu.speedbridge2.common.schematic.SchematicHandler;
import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.object.World;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import com.github.tofpu.speedbridge2.service.manager.ServiceManager;
import io.github.tofpu.speedbridge.gameengine.BaseGameHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BridgeSetupHandler extends BaseGameHandler<IslandSetupData> {

    private final IslandService islandService;
    private final PlatformArenaAdapter arenaAdapter;
    private final LandController landController;
    private final SchematicHandler schematicHandler;

    private final GameRegistry<IslandSetup> gameRegistry = new GameRegistry<>();

    public BridgeSetupHandler(ServiceManager serviceManager, PlatformArenaAdapter arenaAdapter, SchematicHandler schematicHandler) {
        this(serviceManager.get(IslandService.class), arenaAdapter, schematicHandler);
    }

    public BridgeSetupHandler(IslandService islandService, PlatformArenaAdapter arenaAdapter, SchematicHandler schematicHandler) {
        this.islandService = islandService;
        this.arenaAdapter = arenaAdapter;
        this.landController = new LandController(new SetupArenaManager(arenaAdapter));
        this.schematicHandler = schematicHandler;
    }

    @Override
    public void registerStates() {
        this.stateManager.addState(IslandSetupStates.START, new StartSetupState());
        this.stateManager.addState(IslandSetupStates.STOP, new StopSetupState());
        this.stateManager.addState(IslandSetupStates.SET_ORIGIN, new SetOriginState());
    }

    public void start(final OnlinePlayer player, final int slot, final String schematicName) {
        if (gameRegistry.isInGame(player.id())) {
            return;
        }
        IslandSetup game = prepareGameObject(player, slot, schematicName);
        game.dispatch(IslandSetupStates.START);
    }

    @NotNull
    private IslandSetup prepareGameObject(OnlinePlayer player, int slot, String schematicName) {
        Schematic schematic = schematicHandler.resolve(schematicName);
        World world = arenaAdapter.gameWorld();
        Location origin = Location.zero(world);

        Island island = islandService.get(slot);
        if (island != null && island.getAbsolute() != null) {
            origin = island.getAbsolute();
        }

        IslandSchematic islandSchematic = new IslandSchematic(slot, schematic, origin);
        Land land = landController.reserveSpot(player.id(), islandSchematic, world);

        IslandSetupData data = new IslandSetupData(new SetupPlayer(player), slot, schematicName, land);
        return new IslandSetup(data, stateManager);
    }

    public void stopGame(final UUID playerId) {
        IslandSetup game = getGameByPlayer(playerId);
        if (game == null) return;

        game.dispatch(IslandSetupStates.STOP);
        gameRegistry.removeByPlayer(playerId);
    }
    @Nullable
    public IslandSetup getGameByPlayer(UUID playerId) {
        return gameRegistry.getByPlayer(playerId);
    }
}
