package io.tofpu.speedbridge2.listener.wrapper;

import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventWrapper<E extends Event> extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final BridgePlayer bridgePlayer;
    private final E event;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public EventWrapper(final BridgePlayer bridgePlayer, final E event) {
        this.bridgePlayer = bridgePlayer;
        this.event = event;
    }

    public E getEvent() {
        return event;
    }

    public boolean isPlaying() {
        return bridgePlayer.isPlaying();
    }

    public BridgePlayer getBridgePlayer() {
        return bridgePlayer;
    }

    public boolean hasTimerStarted() {
        return getGamePlayer().hasTimerStarted();
    }

    public GamePlayer getGamePlayer() {
        return bridgePlayer.getGamePlayer();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
