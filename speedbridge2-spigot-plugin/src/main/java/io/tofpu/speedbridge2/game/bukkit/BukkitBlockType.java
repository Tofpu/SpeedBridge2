package io.tofpu.speedbridge2.game.bukkit;

import io.tofpu.speedbridge2.game.generic.BlockType;
import org.bukkit.Material;

public class BukkitBlockType extends BlockType {
    private final Material material;

    public BukkitBlockType(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }
}
