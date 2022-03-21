package io.tofpu.speedbridge2.domain.common.config.category;

import com.cryptomorin.xseries.XMaterial;
import io.tofpu.speedbridge2.domain.common.umbrella.RunCommandItemAction;
import io.tofpu.speedbridge2.domain.common.umbrella.SerializableUmbrellaItem;
import io.tofpu.speedbridge2.domain.common.util.UmbrellaUtil;
import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
public final class GameCategory {
    @Setting("items")
    private List<SerializableUmbrellaItem> umbrellaItems = new ArrayList<>() {{
        final ItemStack resetItem = UmbrellaUtil.create(XMaterial.RED_DYE, "Reset", "reset the game!");
        final ItemStack leaveItem = UmbrellaUtil.create(XMaterial.RED_BED, "Leave", "leave the game!");

        add(createDefault("reset", resetItem, 7, "RESET_ISLAND"));
        add(createDefault("leave", leaveItem, 8, "sb leave"));
    }};

    public List<SerializableUmbrellaItem> getUmbrellaItems() {
        return umbrellaItems;
    }

    SerializableUmbrellaItem createDefault(final String itemIdentifier, final ItemStack item, final int index, final String command) {
        return new SerializableUmbrellaItem(itemIdentifier, index, item, new RunCommandItemAction(command, null));
    }
}
