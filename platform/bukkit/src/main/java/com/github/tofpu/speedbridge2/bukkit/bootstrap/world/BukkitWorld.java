package com.github.tofpu.speedbridge2.bukkit.bootstrap.world;

import com.github.tofpu.speedbridge2.object.World;

public class BukkitWorld extends World {

    private final org.bukkit.World world;

    public BukkitWorld(org.bukkit.World world) {
        super(world.getName());
        this.world = world;
    }

    public org.bukkit.World getWorld() {
        return world;
    }
}