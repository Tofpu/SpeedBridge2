package com.github.tofpu.speedbridge2.bukkit.util;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemStackBuilder {
    public static ItemStackBuilder newBuilder() {
        return new ItemStackBuilder();
    }

    private String displayName = null;
    private final List<String> loreList = new ArrayList<>();

    public ItemStackBuilder withDisplayName(String displayName) {
        this.displayName = ChatUtil.colorize(displayName);
        return this;
    }

    public ItemStackBuilder addLore(String... lores) {
        for (String lore : lores) {
            loreList.add(ChatUtil.colorize(lore));
        }
        return this;
    }

    public ItemStack build(XMaterial material) {
        ItemStack itemStack = material.parseItem();

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (this.displayName != null) {
            itemMeta.setDisplayName(displayName);
        }
        if (!this.loreList.isEmpty()) {
            itemMeta.setLore(loreList);
        }
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
