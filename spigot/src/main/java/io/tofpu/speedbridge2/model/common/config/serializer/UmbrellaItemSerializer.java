package io.tofpu.speedbridge2.model.common.config.serializer;

import io.tofpu.speedbridge2.model.common.umbrella.SerializableUmbrellaItem;
import io.tofpu.umbrella.domain.item.action.AbstractItemAction;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.UUID;

public final class UmbrellaItemSerializer implements TypeSerializer<SerializableUmbrellaItem> {
    public static final UmbrellaItemSerializer INSTANCE = new UmbrellaItemSerializer();

    private static final ItemStack EMPTY_ITEM = new ItemStack(Material.AIR);

    @Override
    public SerializableUmbrellaItem deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        final int index = node.node("index")
                .getInt(-1);
        final ItemStack itemStack = node.node("item")
                .get(ItemStack.class, EMPTY_ITEM);
        final AbstractItemAction itemAction = node.node("action")
                .get(AbstractItemAction.class);

        return new SerializableUmbrellaItem(UUID.randomUUID()
                .toString(), index, itemStack, itemAction);
    }

    @Override
    public void serialize(final Type type, @Nullable final SerializableUmbrellaItem obj, final ConfigurationNode node) throws SerializationException {
        final ItemStack itemStack = obj.getItemStack();

        node.node("index").set(obj.getIndex());
        node.node("item").set(itemStack);
        node.node("action").set(obj.getItemAction());
    }
}
