package io.tofpu.speedbridge2.listener.wrapper;

import io.tofpu.speedbridge2.domain.player.PlayerService;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractEventWrapper extends EventWrapper<PlayerInteractEvent> {
    public PlayerInteractEventWrapper(final PlayerInteractEvent event) {
        super(PlayerService.INSTANCE.get(event.getPlayer()
                .getUniqueId()), event);
    }
}
