package io.tofpu.speedbridge2.command.parser;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.island.object.extra.GameIsland;
import io.tofpu.speedbridge2.model.player.PlayerService;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.process.ValueResolver;

import static io.tofpu.speedbridge2.model.common.Message.INSTANCE;

@AutoRegister
public final class GameIslandParser extends AbstractLampParser<GameIsland> {
    public GameIslandParser(final LampParseRegistry registry) {
        super(GameIsland.class, registry);
    }

    @Override
    GameIsland parse(final ValueResolver.ValueResolverContext context) {
        context.pop();

        final BridgePlayer player = PlayerService.INSTANCE.get(context.actor().getUniqueId());
        if (player == null) {
            throw new CommandErrorException(BridgeUtil.miniMessageToLegacy(INSTANCE.notLoaded));
        }
        return player.getCurrentGame();
    }
}
