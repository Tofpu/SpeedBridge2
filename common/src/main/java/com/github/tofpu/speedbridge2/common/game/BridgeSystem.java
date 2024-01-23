package com.github.tofpu.speedbridge2.common.game;

import com.github.tofpu.speedbridge2.common.PlatformArenaAdapter;
import com.github.tofpu.speedbridge2.common.PlatformGameAdapter;
import com.github.tofpu.speedbridge2.common.game.score.BridgeScoreService;
import com.github.tofpu.speedbridge2.common.gameextra.land.GameLandReserver;
import com.github.tofpu.speedbridge2.common.game.listener.GameListener;
import com.github.tofpu.speedbridge2.common.gameextra.land.Land;
import com.github.tofpu.speedbridge2.common.gameextra.land.LandController;
import com.github.tofpu.speedbridge2.common.island.Island;
import com.github.tofpu.speedbridge2.common.lobby.LobbyService;
import com.github.tofpu.speedbridge2.common.schematic.SchematicHandler;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import com.github.tofpu.speedbridge2.service.manager.ServiceManager;

import java.util.UUID;

public class BridgeSystem {
    private final EventDispatcherService eventDispatcher;
    private final IslandGameHandler gameHandler;
    private final GameLandReserver landReserver;

    public BridgeSystem(EventDispatcherService eventDispatcher, SchematicHandler schematicHandler, PlatformArenaAdapter arenaAdapter) {
        this.eventDispatcher = eventDispatcher;
        this.gameHandler = new IslandGameHandler(eventDispatcher);

        LandController landController = new LandController(new IslandGameArenaManager(arenaAdapter));
        this.landReserver = new GameLandReserver(arenaAdapter.gameWorld(), schematicHandler, landController);
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

    public GameLandReserver landReserver() {
        return landReserver;
    }
}
