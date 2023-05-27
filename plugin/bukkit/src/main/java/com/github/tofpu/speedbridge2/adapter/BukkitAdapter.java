package com.github.tofpu.speedbridge2.adapter;

import com.github.tofpu.speedbridge2.object.Position;
import com.github.tofpu.speedbridge2.object.World;
import org.bukkit.Location;

public class BukkitAdapter {

    public static World toWorld(org.bukkit.World world) {
        return new World(world.getName());
    }

    public static Position toPosition(Location location) {
        return new Position(toWorld(location.getWorld()), (int) location.getX(),
            (int) location.getY(),
            (int) location.getZ());
    }
}
