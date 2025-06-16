package io.tofpu.speedbridge2.game.service;

import io.github.revxrsal.eventbus.EventBus;
import io.tofpu.speedbridge2.Constants;
import io.tofpu.speedbridge2.arena.Arena;
import io.tofpu.speedbridge2.arena.ArenaManager;
import io.tofpu.speedbridge2.game.GameSupplier;
import io.tofpu.speedbridge2.game.domain.Game;
import io.tofpu.speedbridge2.game.domain.GameStateType;
import io.tofpu.speedbridge2.game.event.GameResetEvent;
import io.tofpu.speedbridge2.game.event.GameScoreEvent;
import io.tofpu.speedbridge2.game.event.GameStartEvent;
import io.tofpu.speedbridge2.game.event.GameStopEvent;
import io.tofpu.speedbridge2.game.infra.config.GameConfigManager;
import io.tofpu.speedbridge2.game.infra.config.experience.GamePlayerExperienceConfiguration;
import io.tofpu.speedbridge2.island.domain.Island;
import io.tofpu.speedbridge2.lobby.LobbyTeleporter;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class GameService implements GameSupplier {
    private final EventBus eventBus;
    private final ArenaManager<Integer> arenaManager;
    private final GameConfigManager gameConfigManager;
    private final LobbyTeleporter lobbyTeleporter;

    private final Map<UUID, Game> gameMap = new HashMap<>();

    public GameService(
            EventBus eventBus, World world,
            GameConfigManager gameConfigManager,
            LobbyTeleporter lobbyTeleporter
    ) {
        this.eventBus = eventBus;
        this.lobbyTeleporter = lobbyTeleporter;
        this.arenaManager = new ArenaManager<>(
                world,
                Constants.ArenaPositioning.GAME.apply(
                        () -> gameConfigManager.getConfigData().arena().gap()));
        this.gameConfigManager = gameConfigManager;
    }

    public boolean startGame(Player player, Island island) {
        UUID playerId = player.getUniqueId();
        // if the player is already in a game
        if (isPlaying(playerId)) {
            return false;
        }

        Arena arena = arenaManager.generateArena(island.slot(), island.schematic());
        Game game = new Game(
                player,
                island,
                arena
        );

//        player.teleport(game.location());
        arena.teleport(player);
        gameMap.put(playerId, game);

        game.setState(GameStateType.START);
        eventBus.post(GameStartEvent.class, game);
        return true;
    }

    public void releaseGame(Player player) {
        ifGamePresent(player.getUniqueId(), this::releaseGame);
    }

    private void releaseGame(Game game) {
        Player player = game.gamePlayer().player();
        UUID playerId = player.getUniqueId();
        gameMap.remove(playerId);
        arenaManager.releaseArena(game.island().slot());

        if (player.isOnline()) {
            lobbyTeleporter.teleportToLobby(player);
        }
    }

    public boolean stopGame(Player player) {
        Game game = gameMap.remove(player.getUniqueId());
        if (game == null) {
            return false;
        }
        game.setState(GameStateType.STOP);
        eventBus.post(GameStopEvent.class, game);

        lobbyTeleporter.teleportToLobby(player);
        releaseGame(game);
        return true;
    }

    @Override
    public void ifGamePresent(UUID playerId, Consumer<Game> consumer) {
        Game game = gameMap.get(playerId);
        if (game != null) {
            consumer.accept(game);
        }
    }

    public Game getGame(UUID playerId) {
        return gameMap.get(playerId);
    }

    public Optional<Game> game(UUID playerId) {
        return Optional.ofNullable(gameMap.get(playerId));
    }

    public boolean isPlaying(UUID playerId) {
        return gameMap.containsKey(playerId);
    }

    public void resetGame(Game game) {
        game.setState(GameStateType.RESET);
        eventBus.post(GameResetEvent.class, game);
        Player bukkitPlayer = game.gamePlayer().player();
        gameExperience().reset().apply(bukkitPlayer);

        game.setState(GameStateType.START);
        bukkitPlayer.teleport(game.location());
        eventBus.post(GameStartEvent.class, game);
    }

    private GamePlayerExperienceConfiguration gameExperience() {
        return gameConfigManager.getConfigData().experience();
    }

    public void addScore(Game game) {
        game.setState(GameStateType.SCORE);
        long elapsedTimerInMillis = game.gamePlayer().elapsedTimerInMillis();
        game.gamePlayer().clearTimer();
        // todo: format the score, register it, increment the total wins, etc.

        Player player = game.gamePlayer().player();
        gameExperience().score().apply(player);
        eventBus.post(GameScoreEvent.class, game);

        player.teleport(game.location());
        game.setState(GameStateType.START);
        eventBus.post(GameStartEvent.class, game);
    }
}
