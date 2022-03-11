package io.tofpu.speedbridge2.command.parser;

import cloud.commandframework.annotations.AnnotationAccessor;
import cloud.commandframework.context.CommandContext;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class GamePlayerParser<C> {
  public GamePlayer parse(
      @NonNull CommandContext<C> context, @NonNull AnnotationAccessor annotationAccessor) {
    if (annotationAccessor.annotation(GamePlayerArgument.class) == null) {
      return null;
    }

    final BridgePlayer bridgePlayer = (BridgePlayer) context.getSender();

    return bridgePlayer.getGamePlayer();
  }
}
