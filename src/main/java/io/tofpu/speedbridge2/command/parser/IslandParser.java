package io.tofpu.speedbridge2.command.parser;

import cloud.commandframework.annotations.AnnotationAccessor;
import cloud.commandframework.context.CommandContext;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.extra.CommonBridgePlayer;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.island.object.extra.EmptyIsland;
import io.tofpu.speedbridge2.domain.island.object.extra.GameIsland;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class IslandParser<C> {
    public Island parse(@NonNull CommandContext<CommonBridgePlayer> context, @NonNull AnnotationAccessor annotationAccessor) {
        if (annotationAccessor.annotation(IslandArgument.class) == null) {
            return null;
        }

        final BridgePlayer bridgePlayer = (BridgePlayer) context.getSender();
        final GamePlayer gamePlayer = bridgePlayer.getGamePlayer();

        if (gamePlayer == null) {
            return new EmptyIsland();
        }

        final GameIsland gameIsland = gamePlayer.getCurrentGame();

        return gameIsland.getIsland();
    }
}
