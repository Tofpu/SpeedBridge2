package io.tofpu.speedbridge2.model.common.util;

import com.cryptomorin.xseries.XMaterial;
import io.tofpu.speedbridge2.model.common.toolbar.RunCommandItemAction;
import io.tofpu.speedbridge2.model.common.toolbar.SerializableToolbarItem;
import io.tofpu.speedbridge2.model.island.object.GameIsland;
import io.tofpu.toolbar.toolbar.ItemSlot;
import io.tofpu.toolbar.toolbar.ToolWithSlot;
import io.tofpu.toolbar.toolbar.tool.Tool;
import io.tofpu.toolbar.toolbar.tool.action.ToolAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.UUID;

public class ToolbarUtil {
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

    public static ToolWithSlot<Tool> toTool(final GameIsland gameIsland, final SerializableToolbarItem serializableToolbarItem) {
        final String identifier = UUID.randomUUID().toString();
        final ItemStack itemStack = serializableToolbarItem.getItem();
        final int index = serializableToolbarItem.getIndex();
        final ToolAction<?> toolAction = getToolAction(gameIsland, serializableToolbarItem);

        return new ToolWithSlot<>(
                new Tool(identifier, itemStack, toolAction),
                ItemSlot.atIndex(index)
        );
    }

    private static @NotNull ToolAction<?> getToolAction(GameIsland gameIsland, SerializableToolbarItem serializableToolbarItem) {
        final SerializableToolbarItem.ToolAction itemActionConfig = serializableToolbarItem.getAction();
        final SerializableToolbarItem.ToolAction.ActionType actionType = itemActionConfig.type();

        final ToolAction<?> toolAction;
        if (actionType == SerializableToolbarItem.ToolAction.ActionType.PERFORM_COMMAND) {
            toolAction = new RunCommandItemAction(itemActionConfig.value(), gameIsland);
        } else {
            throw new IllegalStateException("Unknown tool action type: " + actionType);
        }
        return toolAction;
    }
}
