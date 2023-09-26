package com.github.tofpu.speedbridge2.game;

import com.github.tofpu.speedbridge2.ArenaAdapter;
import com.github.tofpu.speedbridge2.GameAdapter;
import com.github.tofpu.speedbridge2.database.service.DatabaseService;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.game.island.Island;
import com.github.tofpu.speedbridge2.game.island.IslandGameHandler;
import com.github.tofpu.speedbridge2.game.core.arena.ClipboardPaster;
import com.github.tofpu.speedbridge2.game.island.arena.IslandArenaManager;
import com.github.tofpu.speedbridge2.lobby.LobbyService;
import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.object.Position;
import com.github.tofpu.speedbridge2.object.World;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.*;

public class GameHandlerTest {
    private final DatabaseService databaseService = new DatabaseService();
    private final LobbyService lobbyService = new LobbyService(databaseService, new EventDispatcherService());
    private final IslandArenaManager arenaManager = new IslandArenaManager(ArenaAdapter.simple(new World(), ClipboardPaster.empty()));
    private final IslandGameHandler gameHandler = new IslandGameHandler(GameAdapter.empty(), lobbyService, arenaManager);

    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException, TimeoutException {
        databaseService.load();

        lobbyService.updateLocation(new Position(new World(), 10, 10, 10)).get(10, TimeUnit.SECONDS);
        Assertions.assertEquals(lobbyService.position(), new Position(new World(), 10, 10, 10));
    }

    @Test
    void start_and_end_game_test() {
        UUID playerId = UUID.randomUUID();
        World world = mock(World.class);
        Island island = new Island(1, new Island.IslandSchematic(new Location(world, 1, 1, 1, 1, 1), new File("test-resources/island/schematics/test.schematic")));
        OnlinePlayer player = mockPlayer(playerId);

        gameHandler.start(player, island);
        Assertions.assertThrows(Exception.class, () -> gameHandler.start(player, island), "A player cannot be in two games simultaneously");

        Assertions.assertTrue(gameHandler.isInGame(playerId), "The player should have been in a game");
        Assertions.assertDoesNotThrow(() -> gameHandler.stop(playerId), "An error was thrown while attempting to stop an ongoing game");
        Assertions.assertFalse(gameHandler.isInGame(playerId), "The game was stopped, so the player should be no longer be in a game");

        Assertions.assertTrue(arenaManager.hasAvailableLand(island.getSlot()), "The land belonging to the island should have been stored in the reserves");

        Assertions.assertThrows(Exception.class, () -> gameHandler.stop(playerId), "An error should have been thrown since a game cannot be stopped when it's non-existent");
    }

    OnlinePlayer mockPlayer(final UUID id) {
        OnlinePlayer mock = mock(OnlinePlayer.class);
        doReturn(id).when(mock).id();
        return mock;
    }
}
