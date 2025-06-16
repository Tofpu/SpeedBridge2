package io.tofpu.speedbridge2.game.infra.listener;

import io.tofpu.speedbridge2.game.domain.Game;
import io.tofpu.speedbridge2.game.service.GameService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.UUID;

public class GameListener implements Listener {
    private final GameService gameService;

    public GameListener(GameService gameService) {
        this.gameService = gameService;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        Game game = gameService.getGame(playerId);
        if (game == null) {
            return;
        }

        boolean successful =
                game.gamePlayer().removePlacedBlock(event.getBlock().getLocation());
        if (!successful) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void on(ItemSpawnEvent event) {
        event.getEntity().setItemStack(null);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        gameService.releaseGame(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void on(CraftItemEvent event) {
        UUID playerId = event.getWhoClicked().getUniqueId();
        if (!gameService.isPlaying(playerId)) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void on(PlayerDropItemEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        if (!gameService.isPlaying(playerId)) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void on(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        UUID playerId = event.getEntity().getUniqueId();
        if (!gameService.isPlaying(playerId)) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void on(FoodLevelChangeEvent event) {
        UUID entityId = event.getEntity().getUniqueId();
        if (!gameService.isPlaying(entityId)) {
            event.setCancelled(true);
        }
    }
}
