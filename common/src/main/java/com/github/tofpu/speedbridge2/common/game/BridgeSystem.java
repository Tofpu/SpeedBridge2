package com.github.tofpu.speedbridge2.common.game;

import com.github.tofpu.speedbridge2.common.PlatformArenaAdapter;
import com.github.tofpu.speedbridge2.common.PlatformGameAdapter;
import com.github.tofpu.speedbridge2.common.game.listener.GameListener;
import com.github.tofpu.speedbridge2.common.game.score.BridgeScoreService;
import com.github.tofpu.speedbridge2.common.gameextra.land.PlayerLandReserver;
import com.github.tofpu.speedbridge2.common.gameextra.land.object.Land;
import com.github.tofpu.speedbridge2.common.gameextra.land.object.ArenaManagerOptions;
import com.github.tofpu.speedbridge2.common.gameextra.land.BasicLandReserver;
import com.github.tofpu.speedbridge2.common.island.Island;
import com.github.tofpu.speedbridge2.common.lobby.LobbyService;
import com.github.tofpu.speedbridge2.common.schematic.SchematicHandler;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.object.Vector;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import com.github.tofpu.speedbridge2.service.manager.ServiceManager;

import java.util.UUID;

public class BridgeSystem {
    private static final ArenaManagerOptions DEFAULT_OPTIONS = new ArenaManagerOptions(new Vector(0, 100, 0), 10);
    private final EventDispatcherService eventDispatcher;
    private final IslandGameHandler gameHandler;
    private final PlayerLandReserver landReserver;

    public BridgeSystem(EventDispatcherService eventDispatcher, SchematicHandler schematicHandler, PlatformArenaAdapter arenaAdapter) {
        this.eventDispatcher = eventDispatcher;
        this.gameHandler = new IslandGameHandler(eventDispatcher);
        this.landReserver = new PlayerLandReserver(schematicHandler, new BasicLandReserver(arenaAdapter, DEFAULT_OPTIONS));
    }

    public void registerListener(PlatformGameAdapter gameAdapter, ServiceManager serviceManager) {
        eventDispatcher.register(new GameListener(gameAdapter, serviceManager.get(BridgeScoreService.class), serviceManager.get(LobbyService.class), landReserver));
    }

    public boolean joinGame(final OnlinePlayer player, final Island island) {
        if (isInGame(player.id())) {
            return false;
        }

        Land land = landReserver.reserveSpot(player.id(), island);
        return gameHandler.start(player, island, land);
    }

    public IslandGame getGameByPlayer(UUID id) {
        return gameHandler.getGameByPlayer(id);
    }

    public boolean leaveGame(final UUID playerId) {
        return gameHandler.stop(playerId);
    }

    public boolean isInGame(UUID playerId) {
        return gameHandler.isInGame(playerId);
    }

    public PlayerLandReserver landReserver() {
        return landReserver;
    }
}
