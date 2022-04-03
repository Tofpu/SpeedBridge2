package io.tofpu.speedbridge2.listener.wrapper.wrappers;

import io.tofpu.speedbridge2.model.player.PlayerService;
import io.tofpu.speedbridge2.listener.wrapper.EventWrapper;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerInteractEventWrapper extends EventWrapper<PlayerInteractEvent> {
    public static PlayerInteractEventWrapper wrap(final PlayerService playerService,
            final @NotNull PlayerInteractEvent event) {
        return new PlayerInteractEventWrapper(playerService, event);
    }

    private PlayerInteractEventWrapper(final PlayerService playerService,
            final @NotNull PlayerInteractEvent event) {
        super(playerService.getOrDefault(event.getPlayer()
                .getUniqueId()), event);
    }
}
