package com.github.tofpu.speedbridge2.common.setup;

import com.github.tofpu.speedbridge2.common.PlatformArenaAdapter;
import com.github.tofpu.speedbridge2.common.gameextra.land.GameLandReserver;
import com.github.tofpu.speedbridge2.common.gameextra.land.Land;
import com.github.tofpu.speedbridge2.common.gameextra.land.LandController;
import com.github.tofpu.speedbridge2.common.island.Island;
import com.github.tofpu.speedbridge2.common.island.IslandService;
import com.github.tofpu.speedbridge2.common.lobby.LobbyService;
import com.github.tofpu.speedbridge2.common.schematic.SchematicHandler;
import com.github.tofpu.speedbridge2.common.setup.listener.IslandSetupListener;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.object.World;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import com.github.tofpu.speedbridge2.service.manager.ServiceManager;
import io.github.tofpu.speedbridge.gameengine.Game;

import java.util.UUID;

public class GameSetupSystem {
    private final BridgeSetupHandler setupHandler;
    private final EventDispatcherService eventDispatcherService;
    private final World world;
    private final GameLandReserver landReserver;
    private final IslandService islandService;

    public GameSetupSystem(EventDispatcherService eventDispatcherService, PlatformArenaAdapter arenaAdapter, SchematicHandler schematicHandler, IslandService islandService) {
        this.setupHandler = new BridgeSetupHandler(eventDispatcherService);
        this.eventDispatcherService = eventDispatcherService;
        this.world = arenaAdapter.gameWorld();
        this.landReserver = new GameLandReserver(world, schematicHandler, new LandController(new SetupArenaManager(arenaAdapter)));
        this.islandService = islandService;
    }

    public void registerListener(ServiceManager serviceManager) {
        eventDispatcherService.register(new IslandSetupListener(islandService, serviceManager.get(LobbyService.class), landReserver));
    }

    public void startSetup(final OnlinePlayer player, final int slot, final String schematicName) {
        Location origin = getIslandOriginOrZero(slot);
        Land land = this.landReserver.reserveSpot(player.id(), new Island(slot, origin, schematicName));
        setupHandler.start(player, slot, schematicName, land);
    }

    private Location getIslandOriginOrZero(int slot) {
        Location origin = Location.zero(world);
        Island island = islandService.get(slot);
        if (island != null && island.getAbsolute() != null) {
            origin = island.getAbsolute();
        }
        return origin;
    }

    public void cancelSetup(OnlinePlayer player) {
        setupHandler.stopGame(player.id());
    }

    public boolean isInSetup(UUID playerId) {
        return setupHandler.getGameByPlayer(playerId) != null;
    }

    public int getSetupSlot(UUID playerId) {
        Game<IslandSetupData> dataGame = setupHandler.getGameByPlayer(playerId);
        if (dataGame == null) {
            return -1;
        }
        return dataGame.data().slot();
    }

    public void setOrigin(UUID playerId, Location location) {
        IslandSetup setupGame = setupHandler.getGameByPlayer(playerId);
        if (setupGame == null) return;
        setupGame.data().originalLocation(location);
        setupGame.dispatch(IslandSetupStates.SET_ORIGIN);
    }
}
