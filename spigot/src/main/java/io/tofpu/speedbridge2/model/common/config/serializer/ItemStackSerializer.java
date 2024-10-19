package io.tofpu.speedbridge2.model.common.config.serializer;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public final class ItemStackSerializer implements TypeSerializer<ItemStack> {
    public static final ItemStackSerializer INSTANCE = new ItemStackSerializer();

    @Override
    public ItemStack deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        final Material material = node.node("material").get(Material.class, Material.AIR);
        final short durability = (short) node.node("durability").getInt(0);
        final ItemMeta itemMeta = node.node("meta").get(ItemMeta.class);

        final ItemStack itemStack = new ItemStack(material, 1, durability);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public void serialize(final Type type, @Nullable final ItemStack obj, final ConfigurationNode node) throws SerializationException {
        node.node("material").set(obj.getType());
        node.node("durability").set(obj.getDurability());
        node.node("meta").set(obj.getItemMeta());
    }
}
