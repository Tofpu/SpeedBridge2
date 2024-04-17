package io.tofpu.speedbridge2.model.blockmenu.listener;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.listener.GameListener;
import io.tofpu.speedbridge2.model.blockmenu.BlockMenuManager;
import io.tofpu.speedbridge2.model.blockmenu.holder.BlockMenuHolder;
import io.tofpu.speedbridge2.model.common.Message;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.player.PlayerService;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@AutoRegister
public final class BlockMenuListener extends GameListener {
    private final PlayerService playerService;

    public BlockMenuListener(final PlayerService playerService) {
        this.playerService = playerService;
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null ||
                (!(clickedInventory.getHolder() instanceof BlockMenuHolder))) {
            return;
        }
        event.setCancelled(true);

        final BlockMenuManager menuManager = BlockMenuManager.INSTANCE;
        final HumanEntity whoClicked = event.getWhoClicked();
        final UUID uniqueId = whoClicked.getUniqueId();

        final ItemStack itemStack = event.getCurrentItem();
        final Material type = itemStack == null ? Material.AIR : itemStack.getType();
        if (type == Material.AIR) {
            return;
        }

        final BridgePlayer bridgePlayer = playerService.getIfPresent(uniqueId);
        if (bridgePlayer == null || bridgePlayer.getChoseMaterial() == type) {
            return;
        }

        if (!menuManager.canSelectItem(bridgePlayer, itemStack)) {
            return;
        }

        if (menuManager.isInCooldown(uniqueId, remainder -> {
            whoClicked.closeInventory();
            BridgeUtil.sendMessage(whoClicked, String.format(Message.INSTANCE.blockChangeCooldown, remainder));
        })) {
            return;
        }

        bridgePlayer.setChosenMaterial(type);
        menuManager.putInCooldown(uniqueId);

        menuManager.showInventory(bridgePlayer);
    }
}
