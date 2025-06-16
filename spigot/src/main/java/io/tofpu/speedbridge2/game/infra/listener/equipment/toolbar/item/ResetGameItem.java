package io.tofpu.speedbridge2.game.infra.listener.equipment.toolbar.item;

import io.tofpu.speedbridge2.game.service.GameService;
import io.tofpu.toolbar.toolbar.tool.action.ToolAction;
import io.tofpu.toolbar.toolbar.tool.action.ToolActionUtil;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ResetGameItem extends GameItem {

    protected static final String ID = "resetGame";

    public ResetGameItem(ItemStack item, GameService gameService) {
        super(ID, item, ToolActionUtil.listenFor(PlayerInteractEvent.class, handle(gameService)));
    }

    private static ToolAction<PlayerInteractEvent> handle(GameService gameService) {
        return (toolbar, event) ->
                gameService.game(event.getPlayer().getUniqueId()).ifPresent(gameService::resetGame);
    }
}
