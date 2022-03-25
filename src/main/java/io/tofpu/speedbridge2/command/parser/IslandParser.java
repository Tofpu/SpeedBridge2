package io.tofpu.speedbridge2.command.parser;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.island.IslandService;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.process.ValueResolver;

import static io.tofpu.speedbridge2.domain.common.Message.INSTANCE;

@AutoRegister
public final class IslandParser extends AbstractLampParser<Island> {
    public IslandParser(final LampParseRegistry registry) {
        super(Island.class, registry);
    }

    @Override
    Island parse(final ValueResolver.ValueResolverContext context) {
        final BridgePlayer player = PlayerService.INSTANCE.get(context.actor().getUniqueId());

        if (player == null) {
            throw new CommandErrorException(BridgeUtil.miniMessageToLegacy(INSTANCE.notLoaded));
        }

        final String input = context.pop();
        final IslandService islandService = IslandService.INSTANCE;

        int slot;
        try {
            slot = Integer.parseInt(input);
        } catch (NumberFormatException exception) {
            slot = -1;
        }

        Island island = null;
        if (slot != -1) {
            island = islandService.findIslandBy(slot);
        } else if (input != null && !input.isEmpty()) {
            island = islandService.findIslandBy(input);
        }

        if (island == null) {
            if (slot == -1) {
                throw new CommandErrorException(BridgeUtil.miniMessageToLegacy(INSTANCE.invalidIslandArgument));
            }
            throw new CommandErrorException(BridgeUtil.miniMessageToLegacy(String.format(INSTANCE.invalidIsland, slot + "")));
        }

        return island;
    }
}
