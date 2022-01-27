package io.tofpu.speedbridge2.listener.wrapper.wrappers;

import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.listener.wrapper.EventWrapper;
import org.bukkit.event.block.BlockPlaceEvent;

public final class BlockPlaceEventWrapper extends EventWrapper<BlockPlaceEvent> {
    public static BlockPlaceEventWrapper wrap(final BlockPlaceEvent event) {
        return new BlockPlaceEventWrapper(event);
    }

    private BlockPlaceEventWrapper(final BlockPlaceEvent event) {
        super(PlayerService.INSTANCE.get(event.getPlayer()
                .getUniqueId()), event);
    }
}
