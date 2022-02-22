package io.tofpu.speedbridge2.domain.common.config.serializer;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public final class MaterialSerializer implements TypeSerializer<Material> {
    public static final MaterialSerializer INSTANCE = new MaterialSerializer();

    @Override
    public Material deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        final String materialName = node.getString("AIR");

        return XMaterial.matchXMaterial(materialName)
                .orElse(XMaterial.AIR)
                .parseMaterial();
    }

    @Override
    public void serialize(final Type type, @Nullable
    final Material obj, final ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(Material.AIR.name());
            return;
        }
        node.set(obj.name());
    }
}
