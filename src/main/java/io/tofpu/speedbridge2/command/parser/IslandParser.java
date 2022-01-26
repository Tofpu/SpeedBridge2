package io.tofpu.speedbridge2.command.parser;

import cloud.commandframework.annotations.AnnotationAccessor;
import cloud.commandframework.context.CommandContext;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.CommonBridgePlayer;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.island.object.NullIsland;
import io.tofpu.speedbridge2.domain.island.object.GameIsland;
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
            return new NullIsland();
        }

        final GameIsland gameIsland = gamePlayer.getCurrentGame();

        return gameIsland.getIsland();
    }
}
