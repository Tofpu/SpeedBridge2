package io.tofpu.speedbridge2.game.bukkit;

import io.tofpu.speedbridge2.game.generic.Block;
import io.tofpu.speedbridge2.game.generic.Position;
import io.tofpu.speedbridge2.game.generic.World;
import org.bukkit.Material;

public class BukkitBlock extends Block {
    private final Material material;

    public BukkitBlock(World world, Position position, Material material) {
        super(world, position);
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }
}
