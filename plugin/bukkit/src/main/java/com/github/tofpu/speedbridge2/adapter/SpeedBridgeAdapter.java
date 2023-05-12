package com.github.tofpu.speedbridge2.adapter;

import com.github.tofpu.speedbridge2.object.generic.Position;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class SpeedBridgeAdapter {
    public static Location toLocation(Position position) {
        return new Location(toWorld(position.getWorld()), position.getX(), position.getY(), position.getZ());
    }

    public static World toWorld(com.github.tofpu.speedbridge2.object.generic.World world) {
        return Bukkit.getWorld(world.getWorldName());
    }
}
