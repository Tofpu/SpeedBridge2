package io.tofpu.speedbridge2.setup.service;

import io.tofpu.speedbridge2.arena.Arena;
import io.tofpu.speedbridge2.schematic.Schematic;
import io.tofpu.speedbridge2.util.PositionOrientation;
import org.bukkit.entity.Player;

// todo:
//  - add toolbar items
public class IslandSetup {
    private final SetupService setupService;
    private final Player player;
    private final int slot;
    private final Schematic schematic;
    private final Arena arena;

    private PositionOrientation spawnPoint = null;

    public IslandSetup(SetupService setupService, Player player, int slot, Schematic schematic, Arena arena) {
        this.setupService = setupService;
        this.player = player;
        this.slot = slot;
        this.schematic = schematic;
        this.arena = arena;
    }

    public void start() {
        arena.teleport(player);
    }

    public boolean finish() {
        if (!canBeFinished()) {
            return false;
        }

        if (spawnPoint != null) {
            // todo: need to use a location here as we need to
            //  keep track of the yaw & pitch
            this.spawnPoint = arena.getPosition().subtract(spawnPoint);
        }

        setupService.finishSetup(this);
        return true;
    }

    public boolean canBeFinished() {
        https: // github.com/Tofpu/SpeedBridge2/blob/6a5813f99bec1c878610353399379f3204d9571d/spigot/src/main/java/io/tofpu/speedbridge2/model/island/object/setup/IslandSetup.java#L157
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
