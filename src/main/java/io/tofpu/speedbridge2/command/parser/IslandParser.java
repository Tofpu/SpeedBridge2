package io.tofpu.speedbridge2.command.parser;

import cloud.commandframework.annotations.AnnotationAccessor;
import cloud.commandframework.context.CommandContext;
import io.tofpu.speedbridge2.domain.BridgePlayer;
import io.tofpu.speedbridge2.domain.CommonBridgePlayer;
import io.tofpu.speedbridge2.domain.Island;
import io.tofpu.speedbridge2.domain.NullIsland;
import io.tofpu.speedbridge2.domain.game.GameIsland;
import io.tofpu.speedbridge2.domain.game.GamePlayer;
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
