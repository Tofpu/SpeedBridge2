package com.github.tofpu.speedbridge2.lobby;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireArgument;
import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

import com.github.tofpu.speedbridge2.database.service.DatabaseService;
import com.github.tofpu.speedbridge2.listener.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.object.generic.Position;
import com.github.tofpu.speedbridge2.service.LoadableService;
import com.github.tofpu.speedbridge2.service.manager.ServiceManager;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

public class LobbyService implements LoadableService {

    private static final int PRIMARY_LOBBY_ID = 0;

    private final DatabaseService databaseService;
    private final EventDispatcherService eventDispatcherService;
    private Lobby lobby;

    public LobbyService(DatabaseService databaseService,
        EventDispatcherService eventDispatcherService) {
        this.databaseService = databaseService;
        this.eventDispatcherService = eventDispatcherService;
    }

    public LobbyService(ServiceManager serviceManager) {
        this(serviceManager.get(DatabaseService.class), serviceManager.get(EventDispatcherService.class));
    }

    @Override
    public void load() {
        this.eventDispatcherService.register(new LobbyListener(this));
        this.lobby = findLobby();
    }

    @Override
    public void unload() {
    }

    private Lobby findLobby() {
        return databaseService.compute(session -> session.find(Lobby.class, PRIMARY_LOBBY_ID));
    }

    public CompletableFuture<Lobby> fetchLobby() {
        return databaseService.computeAsync(session -> session.find(Lobby.class, PRIMARY_LOBBY_ID));
    }

    public CompletableFuture<Void> updateLocation(Position lobbyPosition) {
        requireArgument(lobbyPosition != null, "A lobby position must not be null.");

        Lobby updatedLobby = new Lobby(0, lobbyPosition);
        requireArgument(updatedLobby.getId() == 0, "A lobby id must be 0, not %s",
            updatedLobby.getId());

        this.lobby = updatedLobby;
        return databaseService.executeAsync(session -> session.persist(updatedLobby));
    }

    public boolean isLobbyAvailable() {
        return this.lobby != null;
    }

    public Lobby lobby() {
        requireState(isLobbyAvailable(), "No lobby was made.");
        return this.lobby;
    }
}