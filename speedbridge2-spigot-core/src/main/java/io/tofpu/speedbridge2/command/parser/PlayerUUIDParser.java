package io.tofpu.speedbridge2.command.parser;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.command.parser.annotation.PlayerUUID;
import io.tofpu.speedbridge2.model.common.Message;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.process.ValueResolver;

import java.util.UUID;

@AutoRegister
public class PlayerUUIDParser extends AbstractLampParser<UUID> {
    public PlayerUUIDParser(final LampParseRegistry registry) {
        super(UUID.class, registry);
    }

    @Override
    UUID parse(final ValueResolver.ValueResolverContext context) {
        final String name = context.pop();

        UUID result;
        if (context.parameter().hasAnnotation(PlayerUUID.class)) {
            final OfflinePlayer player = Bukkit.getOfflinePlayer(name);

            if (!player.hasPlayedBefore()) {
                throw new CommandErrorException(String.format(BridgeUtil.miniMessageToLegacy(Message.INSTANCE.playerDoesntExist), name));
            }
            result = player.getUniqueId();
        } else {
            try {
                result = UUID.fromString(name);
            } catch (final IllegalStateException exception) {
                throw new CommandErrorException(String.format(BridgeUtil.miniMessageToLegacy(Message.INSTANCE.invalidUuid), name));
            }
        }

        return result;
    }
}
