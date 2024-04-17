package io.tofpu.speedbridge2.listener.wrapper.wrappers;

import io.tofpu.speedbridge2.listener.wrapper.EventWrapper;
import io.tofpu.speedbridge2.model.player.PlayerService;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public final class BlockBreakEventWrapper extends EventWrapper<BlockBreakEvent> {
    private BlockBreakEventWrapper(final PlayerService playerService,
                                   final @NotNull BlockBreakEvent event) {
        super(playerService.getOrDefault(event.getPlayer()
                .getUniqueId()), event);
    }

    public static BlockBreakEventWrapper wrap(final PlayerService playerService,
                                              final @NotNull BlockBreakEvent event) {
        return new BlockBreakEventWrapper(playerService, event);
    }
}
