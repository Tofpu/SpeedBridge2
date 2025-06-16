package io.tofpu.speedbridge2.setup.service;

import io.tofpu.multiworldedit.VectorWrapper;
import io.tofpu.speedbridge2.schematic.domain.Schematic;
import io.tofpu.speedbridge2.util.PositionOrientation;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class IslandSetup {
    private final Player player;
    private final int slot;
    private final Schematic schematic;

    private PositionOrientation spawnPoint = null;

    public IslandSetup(Player player, int slot, Schematic schematic) {
        this.player = player;
        this.slot = slot;
        this.schematic = schematic;
    }

    public void handleSetSpawnPoint() {
        Location playerLocation = player.getLocation();
        VectorWrapper absolute = schematic.clipboard().getOrigin().subtract(
                playerLocation.getX(),
                playerLocation.getY(),
                playerLocation.getZ()
        );
        this.spawnPoint = new PositionOrientation(
                absolute.getX(),
                absolute.getY(),
                absolute.getZ(),
                playerLocation.getYaw(),
                playerLocation.getPitch()
        );
        player.sendMessage(ChatColor.YELLOW + "Spawn point set: [%s, %s, %s]".formatted(
                absolute.getX(), absolute.getY(), absolute.getZ()
        ));
    }

    public boolean canBeFinished() {
        // https://github.com/Tofpu/SpeedBridge2/blob/6a5813f99bec1c878610353399379f3204d9571d/spigot/src/main/java/io/tofpu/speedbridge2/model/island/object/setup/IslandSetup.java#L157
        return spawnPoint != null;
    }

    public Player player() {
        return player;
    }

    public int slot() {
        return slot;
    }

    public Schematic schematic() {
        return schematic;
    }

    public PositionOrientation spawnPoint() {
        return spawnPoint;
    }
}
