package io.tofpu.speedbridge2.listener.wrapper.wrappers;

import io.tofpu.speedbridge2.model.player.PlayerService;
import io.tofpu.speedbridge2.listener.wrapper.EventWrapper;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

public final class BlockPlaceEventWrapper extends EventWrapper<BlockPlaceEvent> {
    public static BlockPlaceEventWrapper wrap(final @NotNull BlockPlaceEvent event) {
        return new BlockPlaceEventWrapper(event);
    }

    private BlockPlaceEventWrapper(final @NotNull BlockPlaceEvent event) {
        super(PlayerService.INSTANCE.getOrDefault(event.getPlayer()
                .getUniqueId()), event);
    }
}
