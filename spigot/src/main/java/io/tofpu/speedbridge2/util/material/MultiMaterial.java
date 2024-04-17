package io.tofpu.speedbridge2.util.material;

import com.cryptomorin.xseries.XMaterial;
import io.tofpu.dynamicclass.meta.AutoRegister;
import org.bukkit.Material;

import java.util.*;

@AutoRegister
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

//    final static class MaterialResolvers {
//        static final MaterialResolver BUKKIT_MATERIAL_RESOLVER = new BukkitMaterialResolver();
//        static final MaterialResolver XMATERIAL_MATERIAL_RESOLVER = new XMaterialResolver();
//        static final MaterialResolver EMPTY_RESOLVER = new EmptyResolver();
//    }
//
//    static class XMaterialResolver extends MaterialResolver {
//        @Override
//        Material resolve(String name) {
//            return XMaterial.matchXMaterial(name).orElseThrow(() -> new IllegalArgumentException("Unknown material: %s")).parseMaterial();
//        }
//    }
//
//    static class BukkitMaterialResolver extends MaterialResolver {
//        @Override
//        Material resolve(String name) {
//            Material material = Material.matchMaterial(name);
//            if (material == null) {
//                throwException(name);
//            }
//            return material;
//        }
//    }
//
//    static class EmptyResolver extends MaterialResolver {
//
//        @Override
//        Material resolve(String name) {
//            throwException(name);
//            return null;
//        }
//    }
//
//    abstract static class MaterialResolver {
//        abstract Material resolve(String name);
//
//        void throwException(String name) throws RuntimeException {
//            throw getException(name);
//        }
//
//        RuntimeException getException(String name) {
//            return new IllegalArgumentException("Unknown material: %s");
//        }
//    }
}
