package io.tofpu.speedbridge2.command.parser;

import cloud.commandframework.annotations.AnnotationAccessor;
import cloud.commandframework.context.CommandContext;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.island.object.extra.GameIsland;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

//@AutoRegister
public final class GameIslandParser<C> {
    public GameIsland parse(@NonNull CommandContext<C> context,
            @NonNull AnnotationAccessor annotationAccessor) {
        if (annotationAccessor.annotation(GameIslandArgument.class) == null) {
            return null;
        }

        final BridgePlayer bridgePlayer = (BridgePlayer) context.getSender();
        final GamePlayer gamePlayer = bridgePlayer.getGamePlayer();

        if (gamePlayer == null) {
            return null;
        } else {
            return gamePlayer.getCurrentGame();
        }
    }
}
