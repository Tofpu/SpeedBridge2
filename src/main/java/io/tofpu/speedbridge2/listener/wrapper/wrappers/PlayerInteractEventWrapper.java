package io.tofpu.speedbridge2.listener.wrapper.wrappers;

import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.listener.wrapper.EventWrapper;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerInteractEventWrapper extends EventWrapper<PlayerInteractEvent> {
  public static PlayerInteractEventWrapper wrap(final @NotNull PlayerInteractEvent event) {
    return new PlayerInteractEventWrapper(event);
  }

  private PlayerInteractEventWrapper(final @NotNull PlayerInteractEvent event) {
    super(PlayerService.INSTANCE.get(event.getPlayer().getUniqueId()), event);
  }
}
