package io.tofpu.speedbridge2.model.common.umbrella;

import io.tofpu.umbrella.domain.item.UmbrellaItem;
import io.tofpu.umbrella.domain.item.action.AbstractItemAction;
import org.bukkit.inventory.ItemStack;

public final class SerializableUmbrellaItem {
    private final String identifier;
    private final int index;

    private final ItemStack itemStack;
    private final AbstractItemAction itemAction;

    public SerializableUmbrellaItem(final String identifier, final int index, final ItemStack itemStack, final AbstractItemAction itemAction) {
        this.identifier = identifier;
        this.index = index;
        this.itemStack = itemStack;
        this.itemAction = itemAction;
    }

    public SerializableUmbrellaItem(final UmbrellaItem umbrellaItem) {
        this.identifier = umbrellaItem.getItemIdentifier();
        this.index = umbrellaItem.getInventoryIndex();

        this.itemAction = umbrellaItem.getItemAction();
        this.itemStack = umbrellaItem.getCopyOfItem();
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getIndex() {
        return index;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public AbstractItemAction getItemAction() {
        return itemAction;
    }
}
