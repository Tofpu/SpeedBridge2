package io.tofpu.speedbridge2.command.context;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.island.object.extra.GameIsland;
import io.tofpu.speedbridge2.model.player.PlayerService;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.process.ContextResolver;

import static io.tofpu.speedbridge2.model.common.Message.INSTANCE;

@AutoRegister
public final class GameIslandContext extends AbstractLampContext<GameIsland> {
    public GameIslandContext(final LampContextRegistry registry) {
        super(GameIsland.class, registry);
    }

    @Override
    public GameIsland resolve(final ContextResolver.ContextResolverContext context) {
        final BridgePlayer player = PlayerService.INSTANCE.get(context.actor().getUniqueId());
        if (player == null) {
            throw new CommandErrorException(BridgeUtil.miniMessageToLegacy(INSTANCE.notLoaded));
        }
        final GameIsland currentGame = player.getCurrentGame();
        if (currentGame == null) {
            throw new CommandErrorException(BridgeUtil.miniMessageToLegacy(INSTANCE.notInAIsland));
        }

        return currentGame;
    }
}
