package io.tofpu.speedbridge2.util.material;

import com.cryptomorin.xseries.XMaterial;
import io.tofpu.dynamicclass.meta.AutoRegister;
import org.bukkit.Material;

import java.util.*;

@AutoRegister
// todo: refactor to add support for items to add red_wool support and among others
// - player choice
// - this one
// - gui, perhaps? maybe not
public final class MultiMaterial {
    private static final Map<String, Material> GENERAL_MATERIAL_LOOKUP_MAP = new HashMap<>();
    private static final Set<String> BLOCK_MATERIALS = new HashSet<>();

    static {
        for (Material material : Material.values()) {
            addToRegistry(material);
        }
        for (XMaterial material : XMaterial.values()) {
            Material parseMaterial = material.parseMaterial();
            if (parseMaterial == null) continue;
            addToRegistry(parseMaterial);
        }
    }

    private static void addToRegistry(Material material) {
        String materialName = material.name();
        GENERAL_MATERIAL_LOOKUP_MAP.put(materialName, material);

        if (material.isBlock()) {
            BLOCK_MATERIALS.add(materialName);
        }
    }

    public static Material getOrThrow(String name) {
        Material material = GENERAL_MATERIAL_LOOKUP_MAP.get(name);
        if (material == null) {
            throw new IllegalArgumentException("Unknown material: %s");
        }
        return material;
    }

    public static Collection<String> materials() {
        return materials(MaterialCategory.ANY);
    }

    public static Collection<String> materials(MaterialCategory category) {
        switch (category) {
            case ANY:
                return Collections.unmodifiableCollection(GENERAL_MATERIAL_LOOKUP_MAP.keySet());
            case BLOCK:
                System.out.println(String.format("#Materials() returning %s", BLOCK_MATERIALS));
                return Collections.unmodifiableCollection(BLOCK_MATERIALS);
            default:
                throw new RuntimeException(String.format("Unknown material category: %s", category));
        }
    }
}
