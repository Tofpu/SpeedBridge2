package java.com.github.tofpu.speedbridge2.lobby;

import com.github.tofpu.speedbridge2.database.service.DatabaseService;
import com.github.tofpu.speedbridge2.listener.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.lobby.Lobby;
import com.github.tofpu.speedbridge2.lobby.LobbyService;
import com.github.tofpu.speedbridge2.object.generic.Position;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LobbyServiceTest {

    private final DatabaseService databaseService = new DatabaseService();
    private final EventDispatcherService eventDispatcherService = new EventDispatcherService();
    private final LobbyService lobbyService = new LobbyService(databaseService, eventDispatcherService);

    @BeforeEach
    void setUp() {
        databaseService.load();
        lobbyService.load();
    }

    @AfterEach
    void tearDown() {
        databaseService.unload();
        lobbyService.unload();
    }

    @Test
    void invalid_position_test() {
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> lobbyService.updateLocation(null).get(5, TimeUnit.SECONDS));
    }

    @Test
    void basic_test() throws ExecutionException, InterruptedException, TimeoutException {
        Assertions.assertFalse(lobbyService.isLobbyAvailable());
        Assertions.assertThrows(IllegalStateException.class, lobbyService::lobby);

        Position lobbyPosition = new Position(new DemoWorld("demo_world"), 100, 100, 100);

        lobbyService.updateLocation(lobbyPosition).get(5, TimeUnit.SECONDS);
        Assertions.assertNotNull(lobbyService.lobby());

        Lobby fetchedValue = lobbyService.fetchLobby().get(5, TimeUnit.SECONDS);
        Assertions.assertEquals(fetchedValue.getPosition(), lobbyPosition);

        // makes sure that the lobby service's load functionality is not a bluff
        final LobbyService newLobbyService = new LobbyService(databaseService,eventDispatcherService);
        newLobbyService.load();
        Assertions.assertEquals(newLobbyService.lobby().getPosition(), lobbyPosition);
    }
}