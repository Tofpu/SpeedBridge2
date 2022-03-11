package io.tofpu.speedbridge2.listener.wrapper.wrappers;

import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.listener.wrapper.EventWrapper;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public final class BlockBreakEventWrapper extends EventWrapper<BlockBreakEvent> {
  public static BlockBreakEventWrapper wrap(final @NotNull BlockBreakEvent event) {
    return new BlockBreakEventWrapper(event);
  }

  private BlockBreakEventWrapper(final @NotNull BlockBreakEvent event) {
    super(PlayerService.INSTANCE.get(event.getPlayer().getUniqueId()), event);
  }
}
