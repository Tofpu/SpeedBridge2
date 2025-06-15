package io.tofpu.speedbridge2.game;

import io.tofpu.speedbridge2.Constants;
import io.tofpu.speedbridge2.arena.Arena;
import io.tofpu.speedbridge2.arena.ArenaManager;
import io.tofpu.speedbridge2.game.config.GameConfigManager;
import io.tofpu.speedbridge2.game.listener.GameListener;
import io.tofpu.speedbridge2.game.state.GameStateProvider;
import io.tofpu.speedbridge2.game.state.GameStateType;
import io.tofpu.speedbridge2.game.toolbar.GameEquipmentHandler;
import io.tofpu.speedbridge2.island.domain.Island;
import io.tofpu.speedbridge2.lobby.LobbyTeleporter;
import io.tofpu.speedbridge2.util.listener.ListenerRegistration;
import io.tofpu.toolbar.ToolbarAPI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class GameService implements GameSupplier {
    private final ArenaManager<Integer> arenaManager;
    private final GameConfigManager gameConfigManager;
    private final ListenerRegistration listenerRegistration;
    private final LobbyTeleporter lobbyTeleporter;
    private final GameEquipmentHandler equipmentHandler;

    private final Map<UUID, Game> gameMap = new HashMap<>();

    public GameService(
            World world,
            GameConfigManager gameConfigManager,
            ListenerRegistration listenerRegistration, LobbyTeleporter lobbyTeleporter,
            ToolbarAPI toolbarAPI) {
        this.lobbyTeleporter = lobbyTeleporter;
        this.arenaManager = new ArenaManager<>(
                world,
                Constants.ArenaPositioning.GAME.apply(
                        () -> gameConfigManager.getConfigData().arena().gap()));
        this.gameConfigManager = gameConfigManager;
        this.listenerRegistration = listenerRegistration;
        this.equipmentHandler = new GameEquipmentHandler(this, gameConfigManager, toolbarAPI);
    }

    public void enable() {
        equipmentHandler.register();
        listenerRegistration.register(new GameListener(this));
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
                arena,
                gameConfigManager.getConfigData().experience(),
                new GameStateProvider(this::releaseGame, listenerRegistration, equipmentHandler));
        gameMap.put(playerId, game);

        game.setState(GameStateType.START);

        return true;
    }

    public void releaseGame(Player player) {
        ifGamePresent(player.getUniqueId(), this::releaseGame);
    }

    private void releaseGame(Game game) {
        game.stateProvider().unregisterAllListeners();

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
        // we don't need to call the releaseGame method as the game will
        // call the onStopConsumer consumer when it's stopped
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

    public boolean isPlaying(UUID playerId) {
        return gameMap.containsKey(playerId);
    }
}
