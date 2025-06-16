package io.tofpu.speedbridge2.game.toolbar.item;

import io.tofpu.speedbridge2.game.GameSupplier;
import io.tofpu.speedbridge2.game.state.GameStateType;
import io.tofpu.toolbar.toolbar.tool.action.ToolAction;
import io.tofpu.toolbar.toolbar.tool.action.ToolActionUtil;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class LeaveGameItem extends GameItem {

    protected static final String ID = "leaveGame";

    public LeaveGameItem(ItemStack item, GameSupplier gameSupplier) {
        super(ID, item, ToolActionUtil.listenFor(PlayerInteractEvent.class, handle(gameSupplier)));
    }

    private static ToolAction<PlayerInteractEvent> handle(GameSupplier gameSupplier) {
        return (toolbar, event) ->
                gameSupplier.ifGamePresent(event.getPlayer().getUniqueId(), game -> game.setState(GameStateType.STOP));
    }
}
