package io.tofpu.speedbridge2.listener.wrapper.wrappers;

import io.tofpu.speedbridge2.listener.wrapper.EventWrapper;
import io.tofpu.speedbridge2.model.player.PlayerService;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

public final class BlockPlaceEventWrapper extends EventWrapper<BlockPlaceEvent> {
    private BlockPlaceEventWrapper(final PlayerService playerService,
                                   final @NotNull BlockPlaceEvent event) {
        super(playerService.getOrDefault(event.getPlayer()
                .getUniqueId()), event);
    }

    public static BlockPlaceEventWrapper wrap(final PlayerService playerService,
                                              final @NotNull BlockPlaceEvent event) {
        return new BlockPlaceEventWrapper(playerService, event);
    }
}
