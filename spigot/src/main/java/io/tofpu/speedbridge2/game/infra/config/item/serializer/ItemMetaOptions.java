package io.tofpu.speedbridge2.game.infra.config.item.serializer;

import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemMetaOptions {
    private final String displayName;
    private final List<String> lore;

    public ItemMetaOptions(String displayName, List<String> lore) {
        this.displayName = displayName;
        this.lore = lore;
    }

    public ItemMetaOptions(ItemMeta itemMeta) {
        this(itemMeta.getDisplayName(), itemMeta.getLore());
    }

    public ItemStack apply(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public String displayName() {
        return displayName;
    }

    public List<String> lore() {
        return lore;
    }
}
