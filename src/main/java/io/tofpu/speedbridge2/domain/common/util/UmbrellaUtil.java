package io.tofpu.speedbridge2.domain.common.util;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class UmbrellaUtil {
    public static ItemStack create(final XMaterial material, final String displayName,
            final String lore) {
        final ItemStack itemStack = material.parseItem();
        final ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(BridgeUtil.translateMiniMessageLegacy("<yellow>" + displayName));
        meta.setLore(Collections.singletonList(BridgeUtil.translateMiniMessageLegacy(
                "<gray>Click to " + lore)));

        itemStack.setItemMeta(meta);

        return itemStack;
    }
}
