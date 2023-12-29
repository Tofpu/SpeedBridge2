package com.github.tofpu.speedbridge2.bukkit.helper;

import com.github.tofpu.speedbridge2.object.Position;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class BukkitConversionHelper {

    public static Location toLocation(Position position) {
        return new Location(toWorld(position.getWorld()), position.getX(), position.getY(),
            position.getZ());
    }

    public static Location toLocation(com.github.tofpu.speedbridge2.object.Location location) {
        return new Location(toWorld(location.getWorld()), location.getX(), location.getY(),
                location.getZ(), location.getYaw(), location.getPitch());
    }

    public static World toWorld(com.github.tofpu.speedbridge2.object.World world) {
        return Bukkit.getWorld(world.getWorldName());
    }
}
