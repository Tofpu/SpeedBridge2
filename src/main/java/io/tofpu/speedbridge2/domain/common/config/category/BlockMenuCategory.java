package io.tofpu.speedbridge2.domain.common.config.category;

import org.bukkit.Material;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ConfigSerializable
public final class BlockMenuCategory {
    @Setting("blocks")
    private Set<Material> materials = new HashSet<>(Arrays.asList(Material.WOOL,
            Material.DIAMOND_BLOCK, Material.GOLD_BLOCK, Material.IRON_BLOCK,
            Material.REDSTONE_BLOCK, Material.LAPIS_BLOCK, Material.QUARTZ_BLOCK));

    public Set<Material> getMaterialBlocks() {
        return materials;
    }
}
