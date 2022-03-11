package io.tofpu.speedbridge2.listener.wrapper;

import io.tofpu.speedbridge2.domain.island.object.extra.GameIsland;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class EventWrapper<E extends Event> extends Event {
  private static final @NotNull HandlerList handlers = new HandlerList();

  private final @NotNull BridgePlayer bridgePlayer;
  private final @NotNull E event;

  public static HandlerList getHandlerList() {
    return handlers;
  }

  public EventWrapper(final @NotNull BridgePlayer bridgePlayer, final @NotNull E event) {
    this.bridgePlayer = bridgePlayer;
    this.event = event;
  }

  public @NotNull E getEvent() {
    return event;
  }

  public boolean isPlaying() {
    return bridgePlayer.isPlaying();
  }

  public @NotNull BridgePlayer getBridgePlayer() {
    return bridgePlayer;
  }

  public boolean hasTimerStarted() {
    return getGamePlayer().hasTimerStarted();
  }

  public @NotNull GamePlayer getGamePlayer() {
    return bridgePlayer.getGamePlayer();
  }

  public @NotNull GameIsland getCurrentGame() {
    return getGamePlayer().getCurrentGame();
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }
}
