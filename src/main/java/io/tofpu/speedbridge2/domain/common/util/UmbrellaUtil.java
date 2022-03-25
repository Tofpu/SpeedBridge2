package io.tofpu.speedbridge2.domain.common.util;

import com.cryptomorin.xseries.XMaterial;
import io.tofpu.speedbridge2.domain.common.umbrella.RunCommandItemAction;
import io.tofpu.speedbridge2.domain.common.umbrella.SerializableUmbrellaItem;
import io.tofpu.speedbridge2.domain.island.object.extra.GameIsland;
import io.tofpu.umbrella.UmbrellaAPI;
import io.tofpu.umbrella.domain.Umbrella;
import io.tofpu.umbrella.domain.item.UmbrellaItem;
import io.tofpu.umbrella.domain.item.action.AbstractItemAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class UmbrellaUtil {
    public static ItemStack create(final XMaterial material, final String displayName,
            final String lore) {
        final ItemStack itemStack = material.parseItem();
        final ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(BridgeUtil.miniMessageToLegacy(
                "<yellow>" + displayName));
        meta.setLore(Collections.singletonList(BridgeUtil.miniMessageToLegacy(
                "<gray>Click to " + lore)));

        itemStack.setItemMeta(meta);

        return itemStack;
    }

    public static UmbrellaItem toUmbrellaItem(final Umbrella umbrella, final GameIsland gameIsland, final SerializableUmbrellaItem serializableUmbrellaItem) {
        final String identifier = serializableUmbrellaItem.getIdentifier();
        final ItemStack itemStack = serializableUmbrellaItem.getItemStack();
        final int index = serializableUmbrellaItem.getIndex();
        AbstractItemAction itemAction = serializableUmbrellaItem.getItemAction();

        if (itemAction instanceof RunCommandItemAction) {
            final RunCommandItemAction commandItemAction = (RunCommandItemAction) itemAction;
            itemAction = new RunCommandItemAction(commandItemAction.getCommand(), gameIsland);
        }

        return UmbrellaAPI.getInstance()
                .getUmbrellaService()
                .getUmbrellaItemFactory()
                .create(umbrella, identifier, itemStack, index, itemAction);
    }
}
