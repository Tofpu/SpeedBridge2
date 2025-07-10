package io.tofpu.speedbridge2.block.domain;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public record Block(
        String id,
        String viewPermission,
        ItemStack item
) {
    public boolean test(Player player) {
        if (viewPermission == null || viewPermission.isEmpty()) {
            return true; // No permission required
        }
        return player.hasPermission(viewPermission);
    }

    @Override
    public ItemStack item() {
        return item.clone();
    }
}
