package io.tofpu.speedbridge2.model.common.config.category;

import com.cryptomorin.xseries.XMaterial;
import io.tofpu.speedbridge2.model.common.toolbar.SerializableToolbarItem;
import io.tofpu.speedbridge2.model.common.util.ToolbarUtil;
import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
public final class GameCategory {
    @Setting("items")
    private List<SerializableToolbarItem> items = new ArrayList() {{
        final ItemStack resetItem = ToolbarUtil.create(XMaterial.RED_DYE, "Reset", "reset the game!");
        final ItemStack leaveItem = ToolbarUtil.create(XMaterial.RED_BED, "Leave", "leave the game!");

        add(createDefault(resetItem, 7, "RESET_ISLAND"));
        add(createDefault(leaveItem, 8, "sb leave"));
    }};

    public List<SerializableToolbarItem> getToolbarItems() {
        return items;
    }

    SerializableToolbarItem createDefault(final ItemStack item, final int index, final String command) {
        return new SerializableToolbarItem(index, item, new SerializableToolbarItem.ToolAction(
                SerializableToolbarItem.ToolAction.ActionType.PERFORM_COMMAND,
                command
        ));
    }
}
