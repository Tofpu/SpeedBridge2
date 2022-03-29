package io.tofpu.speedbridge2.command.condition;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.command.condition.annotation.RestrictSetup;
import io.tofpu.speedbridge2.model.common.Message;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.player.PlayerService;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
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

        final RestrictSetup annotation = command.getAnnotation(RestrictSetup.class);
        if (annotation.opposite()) {
            if (!player.isInSetup()) {
                throw new CommandErrorException(BridgeUtil.miniMessageToLegacy(Message.INSTANCE.notInASetup));
            }
            return;
        }

        if (player.isInSetup()) {
            throw new CommandErrorException(BridgeUtil.miniMessageToLegacy(Message.INSTANCE.inASetup));
        }
    }
}
