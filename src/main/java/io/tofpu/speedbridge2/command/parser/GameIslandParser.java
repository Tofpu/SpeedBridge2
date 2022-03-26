package io.tofpu.speedbridge2.command.parser;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.domain.island.object.extra.GameIsland;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import revxrsal.commands.process.ValueResolver;

@AutoRegister
public final class GameIslandParser extends AbstractLampParser<GameIsland> {
    public GameIslandParser(final LampParseRegistry registry) {
        super(GameIsland.class, registry);
    }

    @Override
    GameIsland parse(final ValueResolver.ValueResolverContext context) {
        context.pop();

        final BridgePlayer bridgePlayer = context.actor();
        return bridgePlayer.getCurrentGame();
    }
}
