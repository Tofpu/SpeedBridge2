package io.tofpu.speedbridge2.listener.wrapper.wrappers;

import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.listener.wrapper.EventWrapper;
import org.bukkit.event.block.BlockBreakEvent;

public final class BlockBreakEventWrapper extends EventWrapper<BlockBreakEvent> {
    public static BlockBreakEventWrapper wrap(final BlockBreakEvent event) {
        return new BlockBreakEventWrapper(event);
    }

    private BlockBreakEventWrapper(final BlockBreakEvent event) {
        super(PlayerService.INSTANCE.get(event.getPlayer()
                .getUniqueId()), event);
    }
}
