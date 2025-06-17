package io.tofpu.speedbridge2.setup.service;

import io.tofpu.speedbridge2.arena.Arena;
import io.tofpu.speedbridge2.arena.CuboidRegion;
import io.tofpu.speedbridge2.positioning.PositionOffset;
import io.tofpu.speedbridge2.schematic.domain.Schematic;
import io.tofpu.speedbridge2.util.PositionOrientation;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class IslandSetup {
    private final Player player;
    private final int slot;
    private final Schematic schematic;
    private final Arena arena;

    private PositionOffset spawnPoint;

    public IslandSetup(Player player, int slot, Schematic schematic, Arena arena, PositionOffset spawnPoint) {
        this.player = player;
        this.slot = slot;
        this.schematic = schematic;
        this.arena = arena;
        this.spawnPoint = spawnPoint;
    }

    public void teleport(Player player) {
        Location location;
        if (spawnPoint != null) {
            location = spawnPoint.applyTo(arena.getPosition()).toLocation(arena.world());
        } else {
            location = arena.getPosition().toLocation(arena.world());
        }
        player.teleport(location);
    }

    public void handleSetSpawnPoint() {
        Location playerLocation = player.getLocation();
        if (!region().contains(playerLocation.toVector())) {
            player.sendMessage(ChatColor.RED + "You cannot set the spawn point outside the island region!");
            return;
        }
        Location playerLocationMinusArena = arena.getPosition()
                .subtract(new PositionOrientation(
                                playerLocation.getBlockX(),
                                playerLocation.getBlockY(),
                                playerLocation.getBlockZ(),
                                playerLocation.getYaw(),
                                playerLocation.getPitch()
                        )
                ).toLocation(arena.world());
        this.spawnPoint = new PositionOffset(
                new PositionOrientation(
                        playerLocationMinusArena.getX(),
                        playerLocationMinusArena.getY(),
                        playerLocationMinusArena.getZ(),
                        playerLocation.getYaw(),
                        playerLocation.getPitch()
                )
        );
        PositionOrientation offset = spawnPoint.offset();
        player.sendMessage(ChatColor.YELLOW + "Spawn point set: [%s, %s, %s]".formatted(
                offset.getX(), offset.getY(), offset.getZ()
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

    public CuboidRegion region() {
        return arena.getRegion();
    }

    public PositionOffset spawnPoint() {
        return spawnPoint;
    }

    public World world() {
        return arena.world();
    }
}
