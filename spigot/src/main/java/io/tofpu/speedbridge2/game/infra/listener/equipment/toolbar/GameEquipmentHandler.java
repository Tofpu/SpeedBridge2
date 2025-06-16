package io.tofpu.speedbridge2.game.infra.listener.equipment.toolbar;

import static io.tofpu.speedbridge2.game.infra.listener.equipment.toolbar.GameToolbarHelper.toolItemMapper;

import io.tofpu.speedbridge2.game.GameSupplier;
import io.tofpu.speedbridge2.game.infra.config.GameConfigManager;
import io.tofpu.speedbridge2.game.infra.config.GameConfiguration;
import io.tofpu.speedbridge2.game.infra.config.item.GameItemConfiguration;
import io.tofpu.speedbridge2.game.infra.listener.equipment.toolbar.item.GameItem;
import io.tofpu.speedbridge2.game.infra.listener.equipment.toolbar.item.LeaveGameItem;
import io.tofpu.speedbridge2.game.infra.listener.equipment.toolbar.item.ResetGameItem;
import io.tofpu.toolbar.ToolbarAPI;
import io.tofpu.toolbar.toolbar.ToolWithSlot;
import org.bukkit.entity.Player;

public class GameEquipmentHandler {
    protected static final String TOOLBAR_IDENTIFIER = "game";

    private final GameSupplier gameSupplier;
    private final GameConfigManager gameConfigManager;
    private final ToolbarAPI toolbarAPI;

    public GameEquipmentHandler(GameSupplier gameSupplier, GameConfigManager gameConfigManager, ToolbarAPI toolbarAPI) {
        this.gameSupplier = gameSupplier;
        this.gameConfigManager = gameConfigManager;
        this.toolbarAPI = toolbarAPI;
    }

    public void register() {
        GameConfiguration configData = gameConfigManager.getConfigData();
        GameItemConfiguration itemConfig = configData.item();
        //noinspection unchecked
        ToolWithSlot<GameItem>[] gameItems = new ToolWithSlot[] {
            toolItemMapper(itemConfig.resetGame(), item -> new ResetGameItem(item.item(), gameSupplier)),
            toolItemMapper(itemConfig.leaveGame(), item -> new LeaveGameItem(item.item(), gameSupplier))
        };
        this.toolbarAPI.registerToolbar(new GameToolbar(TOOLBAR_IDENTIFIER, gameItems));
    }

    public void equip(Player player) {
        this.toolbarAPI.equip(TOOLBAR_IDENTIFIER, player);
    }

    public void unequip(Player player) {
        this.toolbarAPI.unequip(player);
    }
}
