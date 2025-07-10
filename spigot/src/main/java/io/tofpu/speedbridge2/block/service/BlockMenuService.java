package io.tofpu.speedbridge2.block.service;

import io.tofpu.speedbridge2.block.domain.Block;
import io.tofpu.speedbridge2.block.domain.menu.BlockMenuSettings;
import io.tofpu.speedbridge2.block.domain.menu.MenuBlockRegistry;
import io.tofpu.speedbridge2.util.listener.ListenerRegistration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.Menu;
import org.ipvp.canvas.MenuFunctionListener;
import org.ipvp.canvas.mask.Mask;
import org.ipvp.canvas.slot.ClickOptions;
import org.ipvp.canvas.slot.Slot;
import org.ipvp.canvas.template.ItemStackTemplate;
import org.ipvp.canvas.type.ChestMenu;

import java.util.Iterator;
import java.util.Objects;

public class BlockMenuService {
    private final BlockService blockService;
    private final BlockMenuSettings menuSettings;
    private final MenuBlockRegistry registry;

    public BlockMenuService(BlockService blockService, BlockMenuSettings menuSettings, MenuBlockRegistry registry) {
        this.blockService = blockService;
        this.menuSettings = menuSettings;
        this.registry = registry;
    }

    public void registerListener(ListenerRegistration listenerRegistration) {
        listenerRegistration.register(new MenuFunctionListener());
    }

    Menu createMenu() {
        ChestMenu menu = ChestMenu.builder(menuSettings.rows())
                .title(menuSettings.title())
                .build();

        BlockMenuSettings.Pattern pattern = menuSettings.pattern();

        // create the mask for the menu
        Mask mask = pattern.createMask(menu);
        // apply pattern to the menu
        mask.apply(menu);
        // deny all click interactions for the slots associated with the mask
        menu.getSlots(mask).forEach(slot -> slot.setClickOptions(ClickOptions.DENY_ALL));
        return menu;
    }

    void populateMenuWithBlockItems(Menu menu, Player viewer) {
        Objects.requireNonNull(menu, "Menu cannot be null");
        Objects.requireNonNull(viewer, "Viewer cannot be null");

        Iterator<Block> blockIterator = registry.all().iterator();
        for (Slot slot : menu.getSlots()) {
            ItemStackTemplate itemTemplate = slot.getSettings().getItemTemplate();
            if (itemTemplate != null) {
                ItemStack item = itemTemplate.getItem(null);
                if (item != null && !item.getType().isAir()) continue; // Skip slots that already have items
            }

            Block selectedBlock = null;
            while (blockIterator.hasNext()) {
                Block next = blockIterator.next();
                if (next.test(viewer)) {
                    selectedBlock = next;
                    break; // Found a block that the viewer can see
                }
            }

            if (selectedBlock == null) {
                break; // No more blocks available that the viewer can see
            }

            slot.setItem(selectedBlock.item());
            slot.setClickOptions(ClickOptions.DENY_ALL);
            Block finalBlock = selectedBlock;
            slot.setClickHandler((player, clickInformation) -> {
                blockService.setBlock(player.getUniqueId(), finalBlock.id());
                player.sendMessage("§aBlock set to " + finalBlock.id());
            });
        }
    }

    public void open(Player viewer) {
        Menu menu = createMenu();
        populateMenuWithBlockItems(menu, viewer);

        if (menu == null) {
            throw new IllegalStateException("Menu has not been created yet. Call createMenu() first.");
        }
        menu.open(viewer);
    }
}
