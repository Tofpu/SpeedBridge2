package io.tofpu.speedbridge2.listener.wrapper;

import io.tofpu.speedbridge2.domain.player.PlayerService;
import org.bukkit.event.block.BlockPlaceEvent;

public final class BlockPlaceEventWrapper extends EventWrapper<BlockPlaceEvent> {
    public BlockPlaceEventWrapper(final BlockPlaceEvent event) {
        super(PlayerService.INSTANCE.get(event.getPlayer()
                .getUniqueId()), event);
    }
}
