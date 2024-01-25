package com.github.tofpu.speedbridge2.common.setup;

import com.github.tofpu.speedbridge2.common.PlatformArenaAdapter;
import com.github.tofpu.speedbridge2.common.PlatformSetupAdapter;
import com.github.tofpu.speedbridge2.common.gameextra.land.PlayerLandReserver;
import com.github.tofpu.speedbridge2.common.gameextra.land.object.Land;
import com.github.tofpu.speedbridge2.common.gameextra.land.object.ArenaManagerOptions;
import com.github.tofpu.speedbridge2.common.gameextra.land.BasicLandReserver;
import com.github.tofpu.speedbridge2.common.island.Island;
import com.github.tofpu.speedbridge2.common.island.IslandService;
import com.github.tofpu.speedbridge2.common.lobby.LobbyService;
import com.github.tofpu.speedbridge2.common.schematic.SchematicHandler;
import com.github.tofpu.speedbridge2.common.setup.listener.IslandSetupListener;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.object.Vector;
import com.github.tofpu.speedbridge2.object.World;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import com.github.tofpu.speedbridge2.service.manager.ServiceManager;
import io.github.tofpu.speedbridge.gameengine.Game;

import java.util.UUID;

public class GameSetupSystem {
    private static final ArenaManagerOptions DEFAULT_OPTIONS = new ArenaManagerOptions(new Vector(0, 100, 100));
    private final IslandSetupHandler setupHandler;
    private final EventDispatcherService eventDispatcherService;
    private final World world;
    private final PlayerLandReserver landReserver;
    private final IslandService islandService;

    public GameSetupSystem(EventDispatcherService eventDispatcherService, PlatformArenaAdapter arenaAdapter, SchematicHandler schematicHandler, IslandService islandService) {
        this.setupHandler = new IslandSetupHandler(eventDispatcherService);
        this.eventDispatcherService = eventDispatcherService;
        this.world = arenaAdapter.gameWorld();
        this.landReserver = new PlayerLandReserver(schematicHandler, new BasicLandReserver(arenaAdapter, DEFAULT_OPTIONS));
        this.islandService = islandService;
    }

    public void registerListener(PlatformSetupAdapter setupAdapter, ServiceManager serviceManager) {
        IslandSetupListener listener = new IslandSetupListener(setupAdapter, islandService, serviceManager.get(LobbyService.class), landReserver);
        eventDispatcherService.register(listener);
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
        setupHandler.stopSetup(player.id());
    }

    public boolean isInSetup(UUID playerId) {
        return setupHandler.getSetupByPlayer(playerId) != null;
    }

    public int getSetupSlot(UUID playerId) {
        Game<IslandSetupData> dataGame = setupHandler.getSetupByPlayer(playerId);
        if (dataGame == null) {
            return -1;
        }
        return dataGame.data().slot();
    }

    public IslandSetup getSetup(UUID playerId) {
        return setupHandler.getSetupByPlayer(playerId);
    }
}
