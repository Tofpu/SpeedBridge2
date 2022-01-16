package io.tofpu.speedbridge2.domain.game;

import io.tofpu.speedbridge2.domain.schematic.SchematicPlot;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class GamePlayer {
    private static final Map<UUID, GamePlayer> GAME_PLAYER_MAP = new HashMap<>();
    private final Player player;

    private boolean queue;
    private int islandSlot;

    public static GamePlayer of(final Player player) {
        return GAME_PLAYER_MAP.computeIfAbsent(player.getUniqueId(), uuid -> new GamePlayer(player));
    }

    private GamePlayer(final Player player) {
        this.player = player;
    }

    public void setIslandSlot(final int islandSlot) {
        this.islandSlot = islandSlot;
    }

    public void startQueue() {
        this.queue = true;
    }

    public void resetQueue() {
        this.queue = false;
    }

    public boolean isInQueue() {
        return queue;
    }

    public boolean isPlaying() {
        return islandSlot != -1;
    }

    public int getIslandSlot() {
        return islandSlot;
    }

    public Player getPlayer() {
        return player;
    }

    public void teleport(final SchematicPlot selectedPlot) {
        if (player == null) {
            return;
        }
        player.teleport(new Location(selectedPlot.getWorld(), selectedPlot.getX(), selectedPlot.getY(), selectedPlot.getZ()));
    }
}
