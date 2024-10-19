package io.tofpu.speedbridge2.listener.game;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.listener.GameListener;
import io.tofpu.speedbridge2.model.player.PlayerService;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public final class GameProtectionListener extends GameListener {
    private final PlayerService playerService;

    public GameProtectionListener(final PlayerService playerService) {
        this.playerService = playerService;
    }

    @EventHandler // skipcq: JAVA-W0324
    private void onItemDrop(final @NotNull PlayerDropItemEvent event) {
        final BridgePlayer bridgePlayer = playerService.getIfPresent(event.getPlayer()
                .getUniqueId());
        if (bridgePlayer == null || !bridgePlayer.isPlaying()) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler // skipcq: JAVA-W0324
    private void onEntityDamage(final @NotNull EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        final Player player = (Player) event.getEntity();
        final BridgePlayer bridgePlayer = playerService.getIfPresent(player
                .getUniqueId());
        if (bridgePlayer == null || !bridgePlayer.isPlaying()) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler // skipcq: JAVA-W0324
    private void onFoodLevelChange(final @NotNull FoodLevelChangeEvent event) {
        final Player player = (Player) event.getEntity();
        final BridgePlayer bridgePlayer = playerService.getIfPresent(player
                .getUniqueId());
        if (bridgePlayer == null || !bridgePlayer.isPlaying()) {
            return;
        }

        event.setCancelled(true);
    }
}
