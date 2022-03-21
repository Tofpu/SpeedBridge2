package io.tofpu.speedbridge2.domain.common.config.serializer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.List;

public final class ItemMetaSerializer implements TypeSerializer<ItemMeta> {
    public static final ItemMetaSerializer INSTANCE = new ItemMetaSerializer();
    private static final ItemMeta ITEM_META = Bukkit.getItemFactory()
            .getItemMeta(Material.DIAMOND_BLOCK);

    @Override
    public ItemMeta deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        final ItemMeta itemMeta = ITEM_META.clone();

        final String displayName = node.node("display_name").getString("");
        final @Nullable List<String> lore = node.node("lore").getList(String.class);

        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        return itemMeta;
    }

    @Override
    public void serialize(final Type type, @Nullable
    final ItemMeta obj, final ConfigurationNode node) throws SerializationException {
        node.node("display_name").set(obj.getDisplayName());
        node.node("lore").set(obj.getLore());
    }
}
