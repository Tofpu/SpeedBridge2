package io.tofpu.speedbridge2.listener.wrapper;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.listener.GameListener;
import io.tofpu.speedbridge2.listener.wrapper.wrappers.BlockBreakEventWrapper;
import io.tofpu.speedbridge2.listener.wrapper.wrappers.BlockPlaceEventWrapper;
import io.tofpu.speedbridge2.listener.wrapper.wrappers.PlayerInteractEventWrapper;
import io.tofpu.speedbridge2.model.player.PlayerService;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public final class SpeedBridgeListener extends GameListener {
    private final PlayerService playerService;

    public SpeedBridgeListener(final PlayerService playerService) {
        this.playerService = playerService;
    }

    @EventHandler // skipcq: JAVA-W0324
    private void onBlockPlace(final @NotNull BlockPlaceEvent event) {
        final EventWrapper<BlockPlaceEvent> eventWrapper =
                BlockPlaceEventWrapper.wrap(playerService, event);
        if (!eventWrapper.isPlaying()) {
            return;
        }

        callEvent(eventWrapper);
    }

    @EventHandler // skipcq: JAVA-W0324
    private void onBlockPlace(final @NotNull BlockBreakEvent event) {
        final EventWrapper<BlockBreakEvent> eventWrapper =
                BlockBreakEventWrapper.wrap(playerService, event);
        if (!eventWrapper.isPlaying()) {
            return;
        }

        callEvent(eventWrapper);
    }

    private void callEvent(final @NotNull Event event) {
        Bukkit.getPluginManager()
                .callEvent(event);
    }

    @EventHandler // skipcq: JAVA-W0324
    private void onPlayerInteract(final @NotNull PlayerInteractEvent event) {
        final EventWrapper<PlayerInteractEvent> eventWrapper = PlayerInteractEventWrapper.wrap(playerService, event);

        if (event.getAction() != Action.PHYSICAL || !eventWrapper.isPlaying() ||
            !eventWrapper.hasTimerStarted()) {
            return;
        }

        // if the clicked block type doesn't happen to be a pressure plate, return
        if (!event.getClickedBlock().getType().name().contains("PLATE")) {
            return;
        }

        callEvent(eventWrapper);
    }
}
