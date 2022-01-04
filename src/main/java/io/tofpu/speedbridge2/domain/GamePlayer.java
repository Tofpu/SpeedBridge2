package io.tofpu.speedbridge2.domain;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GamePlayer {
    private static final Map<UUID, GamePlayer> GAME_PLAYER_MAP = new HashMap<>();
    private final Player player;

    public static GamePlayer of(final Player player) {
        return GAME_PLAYER_MAP.computeIfAbsent(player.getUniqueId(), uuid -> new GamePlayer(player));
    }

    private GamePlayer(final Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
