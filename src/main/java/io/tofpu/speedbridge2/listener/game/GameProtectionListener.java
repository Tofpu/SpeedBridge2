package io.tofpu.speedbridge2.listener.game;

import io.tofpu.speedbridge2.domain.game.GamePlayer;
import io.tofpu.speedbridge2.listener.GameListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public final class GameProtectionListener extends GameListener {
    @EventHandler
    public void onItemDrop(final PlayerDropItemEvent event) {
        final GamePlayer gamePlayer = GamePlayer.of(event.getPlayer());

        if (!gamePlayer.isPlaying()) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(final EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        final Player player = (Player) event.getEntity();
        final GamePlayer gamePlayer = GamePlayer.of(player);

        if (!gamePlayer.isPlaying()) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    private void onFoodLevelChange(final FoodLevelChangeEvent event) {
        final Player player = (Player) event.getEntity();
        final GamePlayer gamePlayer = GamePlayer.of(player);

        if (!gamePlayer.isPlaying()) {
            return;
        }

        event.setCancelled(true);
    }
}
