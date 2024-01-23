package com.github.tofpu.speedbridge2.common.game;

import com.github.tofpu.speedbridge2.common.MockedDatabaseService;
import com.github.tofpu.speedbridge2.common.PlatformArenaAdapter;
import com.github.tofpu.speedbridge2.common.game.event.PlayerScoredEvent;
import com.github.tofpu.speedbridge2.common.game.score.BridgeScoreService;
import com.github.tofpu.speedbridge2.common.gameextra.land.PlayerLandReserver;
import com.github.tofpu.speedbridge2.common.island.Island;
import com.github.tofpu.speedbridge2.common.lobby.LobbyService;
import com.github.tofpu.speedbridge2.common.schematic.SchematicHandler;
import com.github.tofpu.speedbridge2.common.schematic.SchematicResolver;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.object.Position;
import com.github.tofpu.speedbridge2.object.World;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import com.github.tofpu.speedbridge2.service.manager.ServiceManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class BridgeSystemTest {
    private final EventDispatcherService eventDispatcherService = spy(new EventDispatcherService());
    private final LobbyService lobbyService = new LobbyService(new MockedDatabaseService(), eventDispatcherService);
    private final PlatformArenaAdapter arenaAdapter = PlatformArenaAdapter.simple(new World());
    private final BridgeSystem bridgeSystem = new BridgeSystem(eventDispatcherService, SchematicHandler.load(new File("test-resources/island/schematics"), SchematicResolver.empty(), s -> true), arenaAdapter);
    private final PlayerLandReserver landReserver = bridgeSystem.landReserver();

    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException, TimeoutException {
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.register(new BridgeScoreService(eventDispatcherService, mock()));
        serviceManager.register(lobbyService);
        bridgeSystem.registerListener(mock(), serviceManager);

        lobbyService.updateLocation(new Position(new World(), 10, 10, 10)).get(10, TimeUnit.SECONDS);
        Assertions.assertEquals(lobbyService.position(), new Position(new World(), 10, 10, 10));
    }

    @Test
    void start_and_end_game_test() {
        UUID playerId = UUID.randomUUID();
        World world = mock(World.class);
        Island island = new Island(1, new Location(world, 1, 1, 1, 1, 1), "test.schematic");
        OnlinePlayer player = mockPlayer(playerId);

        assertTrue(bridgeSystem.joinGame(player, island));
        assertFalse(bridgeSystem.joinGame(player, island), "A player cannot be in two games simultaneously");

        assertTrue(bridgeSystem.isInGame(playerId), "The player should have been in a game");
        assertTrue(bridgeSystem.leaveGame(playerId), "The game should have been stopped");
        assertFalse(bridgeSystem.isInGame(playerId), "The game was stopped, so the player should be no longer be in a game");

        assertTrue(landReserver.hasAvailableLand(island.getSlot()), "The land belonging to the island should have been stored in the reserves");

        Assertions.assertFalse(() -> bridgeSystem.leaveGame(playerId), "We shouldn't be able to stop a game twice in a row...");
    }

    @Test
    void player_scored_test() {
        UUID playerId = UUID.randomUUID();
        World world = mock(World.class);
        Island island = new Island(1, new Location(world, 1, 1, 1, 1, 1), "test.schematic");
        OnlinePlayer player = mockPlayer(playerId);

        bridgeSystem.joinGame(player, island);
        IslandGame game = bridgeSystem.getGameByPlayer(playerId);

        game.data().beginTimer(true);
        game.dispatch(IslandGameStates.SCORED);

        verify(eventDispatcherService, times(1)).dispatchIfApplicable(isA(PlayerScoredEvent.class));
    }

    OnlinePlayer mockPlayer(final UUID id) {
        OnlinePlayer mock = mock(OnlinePlayer.class);
        doReturn(id).when(mock).id();
        return mock;
    }
}
