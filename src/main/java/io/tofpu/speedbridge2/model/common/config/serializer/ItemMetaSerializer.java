package io.tofpu.speedbridge2.model.common.config.serializer;

import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
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

        final TextComponent deserialize = BukkitComponentSerializer.legacy()
                .deserialize(displayName);

        itemMeta.setDisplayName(BukkitComponentSerializer.legacy().serialize(deserialize));
        itemMeta.setLore(serializeListAdventure(lore));
        return itemMeta;
    }

    @Override
    public void serialize(final Type type, @Nullable
    final ItemMeta obj, final ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw();
            return;
        }

        final TextComponent textComponent = deserialize(obj.getDisplayName());

        node.node("display_name").set(MiniMessage.miniMessage().serialize(textComponent));
        node.node("lore").set(serializeListLegacy(obj.getLore()));
    }

    public List<String> serializeListLegacy(final List<String> list) {
        final List<String> newList = new ArrayList<>();
        for (final String lore : list) {
            newList.add(serialize(deserialize(lore)));
        }
        return newList;
    }

    public TextComponent deserialize(final String input) {
        return LegacyComponentSerializer.legacySection()
                .deserializeOrNull(input);
    }

    public String serialize(final TextComponent component) {
        return MiniMessage.miniMessage().serialize(component);
    }

    public List<String> serializeListAdventure(final List<String> list) {
        if (list == null) {
            return Collections.emptyList();
        }

        final List<String> newList = new ArrayList<>();
        for (final String lore : list) {
            final TextComponent component = BukkitComponentSerializer.legacy()
                    .deserialize(lore);

            newList.add(BukkitComponentSerializer.legacy().serialize(component));
        }
        return newList;
    }
}
