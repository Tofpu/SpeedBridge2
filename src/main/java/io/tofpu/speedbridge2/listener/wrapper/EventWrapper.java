package io.tofpu.speedbridge2.listener.wrapper;

import io.tofpu.speedbridge2.domain.island.object.extra.GameIsland;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventWrapper<E extends Event> extends Event {
    private static final @NotNull HandlerList handlers = new HandlerList();

    private final @Nullable BridgePlayer bridgePlayer;
    private final @NotNull E event;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public EventWrapper(final @Nullable BridgePlayer bridgePlayer,
            final @NotNull E event) {
        this.bridgePlayer = bridgePlayer;
        this.event = event;
    }

    public @NotNull E getEvent() {
        return event;
    }

    public boolean isPlaying() {
        return bridgePlayer != null && bridgePlayer.isPlaying();
    }

    public @Nullable BridgePlayer getBridgePlayer() {
        return bridgePlayer;
    }

    public boolean hasTimerStarted() {
        final GamePlayer gamePlayer = getGamePlayer();
        if (gamePlayer == null) {
            return false;
        }
        return gamePlayer.hasTimerStarted();
    }

    public @Nullable GamePlayer getGamePlayer() {
        if (bridgePlayer == null) {
            return null;
        }
        return bridgePlayer.getGamePlayer();
    }

    public @Nullable GameIsland getCurrentGame() {
        final GamePlayer gamePlayer = getGamePlayer();
        if (gamePlayer == null) {
            return null;
        }
        return gamePlayer.getCurrentGame();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
