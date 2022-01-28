package io.tofpu.speedbridge2.domain.player;

import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class PlayerService {
    public static final PlayerService INSTANCE = new PlayerService();

    private final PlayerHandler playerHandler;
    private final PlayerRepository playerRepository;

    public PlayerService() {
        this.playerHandler = new PlayerHandler();
        this.playerRepository = new PlayerRepository();
    }

    public void load() {
        this.playerRepository.loadPlayers().whenComplete((playerMap,
                throwable) -> {
            this.playerHandler.load(playerMap);
        });
    }

    public BridgePlayer get(final UUID uuid) {
        return this.playerHandler.get(uuid);
    }

    public BridgePlayer remove(final UUID uniqueId) {
        return playerHandler.remove(uniqueId);
    }

    public BridgePlayer internalRefresh(final Player player) {
        return playerHandler.internalRefresh(player.getUniqueId());
    }

    public BridgePlayer invalidate(final Player player) {
        return playerHandler.invalidate(player.getUniqueId());
    }
}
