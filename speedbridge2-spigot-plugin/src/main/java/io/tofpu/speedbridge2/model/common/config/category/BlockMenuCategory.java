package io.tofpu.speedbridge2.model.common.config.category;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.*;

@ConfigSerializable
public final class BlockMenuCategory {

    @Setting("blocks")
    private List<Material> materials =
            new ArrayList<>(Arrays.asList(toMaterial(XMaterial.WHITE_WOOL),
            Material.DIAMOND_BLOCK, Material.GOLD_BLOCK, Material.IRON_BLOCK,
            Material.REDSTONE_BLOCK, Material.LAPIS_BLOCK, Material.QUARTZ_BLOCK));

    @Setting("default_block")
    private Material defaultBlock = materials.isEmpty() ? Material.AIR : materials.get(0);

    public Collection<Material> getMaterialBlocks() {
        return materials.subList(0, Math.min(7, materials.size()));
    }

    public Material getDefaultBlock() {
        return defaultBlock;
    }

    public static Material toMaterial(final XMaterial xMaterial) {
        return xMaterial.parseMaterial();
    }
}
