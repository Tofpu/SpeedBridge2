package io.tofpu.speedbridge2.command.condition;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.command.condition.annotation.RestrictSetup;
import io.tofpu.speedbridge2.domain.common.Message;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.exception.CommandErrorException;

import java.util.List;

@AutoRegister
public final class RestrictSetupCondition extends AbstractCommandConditionWrapper {
    public RestrictSetupCondition(final LampConditionRegistry registry) {
        super(RestrictSetup.class, registry);
    }

    @Override
    public void execute(
            @NotNull final CommandActor actor,
            @NotNull final ExecutableCommand command,
            @NotNull @Unmodifiable final List<String> arguments) {
        final BridgePlayer player = PlayerService.INSTANCE.get(actor.getUniqueId());
        if (player == null) {
            return;
        }

        if (player.isInSetup()) {
            throw new CommandErrorException(BridgeUtil.miniMessageToLegacy(Message.INSTANCE.inASetup));
        }
    }
}
