package io.tofpu.speedbridge2.command.parser;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.player.PlayerService;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.process.ValueResolver;

import static io.tofpu.speedbridge2.model.common.Message.INSTANCE;

@AutoRegister
public class BridgePlayerParser extends AbstractLampParser<BridgePlayer> {
    private final PlayerService playerService;

    public BridgePlayerParser(PlayerService playerService, LampParseRegistry registry) {
        super(BridgePlayer.class, registry);
        this.playerService = playerService;
    }

    @Override
    BridgePlayer parse(ValueResolver.ValueResolverContext context) {
        final String input = context.pop();
        Player player = Bukkit.getPlayer(input);
        BridgePlayer bridgePlayer = player == null ? null : playerService.getIfPresent(player.getUniqueId());
        if (bridgePlayer == null) {
            throw new CommandErrorException(BridgeUtil.miniMessageToLegacy(String.format(INSTANCE.mustBeOnline, input)));
        }
        return bridgePlayer;
    }
}
