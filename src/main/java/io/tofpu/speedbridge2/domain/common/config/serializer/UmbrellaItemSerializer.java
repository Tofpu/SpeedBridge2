package io.tofpu.speedbridge2.domain.common.config.serializer;

import io.tofpu.speedbridge2.domain.common.umbrella.SerializableUmbrellaItem;
import io.tofpu.umbrella.domain.item.action.AbstractItemAction;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public final class UmbrellaItemSerializer implements TypeSerializer<SerializableUmbrellaItem> {
    public static final UmbrellaItemSerializer INSTANCE = new UmbrellaItemSerializer();

    private static final ItemStack EMPTY_ITEM = new ItemStack(Material.AIR);

    @Override
    public SerializableUmbrellaItem deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        final String identifier = node.node("identifier").getString();
        final int index = node.node("index").getInt(-1);
        final ItemStack itemStack = node.node("item").get(ItemStack.class, EMPTY_ITEM);
        final AbstractItemAction itemAction =
                node.node("action").get(AbstractItemAction.class);

        return new SerializableUmbrellaItem(identifier, index, itemStack, itemAction);
    }

    @Override
    public void serialize(final Type type, @Nullable
    final SerializableUmbrellaItem obj, final ConfigurationNode node) throws SerializationException {
        final ItemStack itemStack = obj.getItemStack();

        node.node("identifier").set(obj.getIdentifier());
        node.node("index").set(obj.getIndex());
        node.node("item").set(itemStack);
        node.node("action").set(obj.getItemAction());
    }
}
