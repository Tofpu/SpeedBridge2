package io.tofpu.speedbridge2.block.infra.listener;

import io.tofpu.speedbridge2.block.service.BlockService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {
    private final BlockService blockService;

    public PlayerConnectionListener(BlockService blockService) {
        this.blockService = blockService;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void on(PlayerJoinEvent event) {
        blockService.loadBlock(event.getPlayer().getUniqueId())
                .whenComplete((itemStack, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                    }
                });
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        blockService.invalidate(event.getPlayer().getUniqueId());
    }
}
