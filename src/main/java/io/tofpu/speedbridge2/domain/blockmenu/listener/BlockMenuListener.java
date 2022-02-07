package io.tofpu.speedbridge2.domain.blockmenu.listener;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.domain.blockmenu.holder.BlockMenuHolder;
import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.listener.GameListener;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@AutoRegister
public final class BlockMenuListener extends GameListener {
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory != null &&
            (!(clickedInventory.getHolder() instanceof BlockMenuHolder))) {
            return;
        }
        event.setCancelled(true);

        final ItemStack itemStack = event.getCurrentItem();
        final Material type = itemStack == null ? null : itemStack.getType();
        if (itemStack == null || type == Material.AIR) {
            return;
        }

        final BridgePlayer bridgePlayer = PlayerService.INSTANCE.get(event.getWhoClicked()
                .getUniqueId());
        if (bridgePlayer == null) {
            return;
        }

        bridgePlayer.setChosenMaterial(type);
    }
}
