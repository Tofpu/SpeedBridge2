package com.github.tofpu.speedbridge2.game.island;

import com.github.tofpu.speedbridge2.game.core.Game;
import com.github.tofpu.speedbridge2.game.core.GameHandler;
import com.github.tofpu.speedbridge2.game.core.GamePlayer;
import com.github.tofpu.speedbridge2.game.core.arena.ArenaManager;
import com.github.tofpu.speedbridge2.game.island.arena.Land;
import com.github.tofpu.speedbridge2.game.core.state.StartGameState;
import com.github.tofpu.speedbridge2.game.core.state.StopGameState;
import com.github.tofpu.speedbridge2.lobby.LobbyService;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

public class IslandGameHandler extends GameHandler<OnlinePlayer> {
    protected final LobbyService lobbyService;
    protected final ArenaManager arenaManager;
    private final Map<UUID, Land> playerLandMapping = new HashMap<>();

    public IslandGameHandler(LobbyService lobbyService, ArenaManager arenaManager) {
        this.lobbyService = lobbyService;
        this.arenaManager = arenaManager;
    }

    public void start(final OnlinePlayer player, final Island island) {
        requireState(!isInGame(player.id()), "%s is already in a game!");

        GamePlayer gamePlayer = createGamePlayer(player);

        Land land = this.arenaManager.generate(island);
        playerLandMapping.put(player.id(), land);

        Game game = createGame(gamePlayer, island, land);

        super.internalStart(player.id(), game);
    }

    @Override
    public void stop(UUID playerId) {
        super.stop(playerId);

        Land land = playerLandMapping.get(playerId);
        this.arenaManager.unlock(land);
    }

    protected Game createGame(GamePlayer gamePlayer, Island island, Land land) {
        return new IslandGame(gamePlayer, island, land);
    }

    protected GamePlayer createGamePlayer(OnlinePlayer player) {
        return new IslandGamePlayer(player);
    }

    @Override
    protected StartGameState createStartState() {
        return new IslandStartGameState();
    }

    @Override
    protected StopGameState createStopState() {
        return new IslandStopGameState(lobbyService);
    }

    static class IslandStartGameState extends StartGameState {
        @Override
        public void apply(Game game) {
            apply((IslandGame) game);
        }

        public void apply(IslandGame game) {
            IslandGamePlayer gamePlayer = (IslandGamePlayer) game.gamePlayer();
            gamePlayer.getPlayer().teleport(game.getLand().getIslandLocation());
        }
    }

    static class IslandStopGameState extends StopGameState {
        private final LobbyService lobbyService;

        IslandStopGameState(LobbyService lobbyService) {
            this.lobbyService = lobbyService;
        }

        @Override
        public void apply(Game game) {
            apply((IslandGame) game);
        }

        public void apply(IslandGame game) {
            IslandGamePlayer gamePlayer = (IslandGamePlayer) game.gamePlayer();
            gamePlayer.getPlayer().teleport(lobbyService.position());
        }
    }
}
