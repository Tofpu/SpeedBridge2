package io.tofpu.speedbridge2.listener.wrapper;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.listener.GameListener;
import io.tofpu.speedbridge2.listener.wrapper.wrappers.BlockBreakEventWrapper;
import io.tofpu.speedbridge2.listener.wrapper.wrappers.BlockPlaceEventWrapper;
import io.tofpu.speedbridge2.listener.wrapper.wrappers.PlayerInteractEventWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public final class SpeedBridgeListener extends GameListener {
    @EventHandler
    private void onBlockPlace(final @NotNull BlockPlaceEvent event) {
        final EventWrapper<BlockPlaceEvent> eventWrapper =
                BlockPlaceEventWrapper.wrap(event);
        if (!eventWrapper.isPlaying()) {
            return;
        }

        callEvent(eventWrapper);
    }

    @EventHandler
    private void onBlockPlace(final @NotNull BlockBreakEvent event) {
        final EventWrapper<BlockBreakEvent> eventWrapper = BlockBreakEventWrapper.wrap(event);
        if (!eventWrapper.isPlaying()) {
            return;
        }

        callEvent(eventWrapper);
    }

    private void callEvent(final @NotNull Event event) {
        Bukkit.getPluginManager()
                .callEvent(event);
    }

    @EventHandler
    private void onPlayerInteract(final @NotNull PlayerInteractEvent event) {
        final EventWrapper<PlayerInteractEvent> eventWrapper =
                PlayerInteractEventWrapper.wrap(event);
        if (event.getAction() != Action.PHYSICAL || !eventWrapper.isPlaying() ||
            !eventWrapper.hasTimerStarted()) {
            return;
        }

        callEvent(eventWrapper);
    }

    private BridgePlayer getBridgePlayer(final @NotNull Player player) {
        return PlayerService.INSTANCE.get(player.getUniqueId());
    }
}
