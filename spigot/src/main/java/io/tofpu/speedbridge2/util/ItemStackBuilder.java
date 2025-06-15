package io.tofpu.speedbridge2.util;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackBuilder {

    private int amount = -1;
    private short durability = -1;

    private String displayName;
    private List<String> lore;

    private ItemStackBuilder() {}

    public static ItemStackBuilder newBuilder() {
        return new ItemStackBuilder();
    }

    public ItemStackBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemStackBuilder durability(short durability) {
        this.durability = durability;
        return this;
    }

    public ItemStackBuilder displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public ItemStackBuilder lore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemStackBuilder lore(String... lore) {
        this.lore = Arrays.asList(lore);
        return this;
    }

    public ItemStack build(Material material) {
        ItemStack itemStack = new ItemStack(material);
        if (amount != -1) {
            itemStack.setAmount(amount);
        }
        if (durability != -1) {
            itemStack.setDurability(durability);
        }
        return apply(itemStack);
    }

    public ItemStack apply(ItemStack itemStack) {
        return applyMeta(itemStack);
    }

    private ItemStack applyMeta(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return itemStack;
        }
        if (displayName != null) {
            meta.setDisplayName(displayName);
        }
        if (lore != null) {
            meta.setLore(lore);
        }
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
