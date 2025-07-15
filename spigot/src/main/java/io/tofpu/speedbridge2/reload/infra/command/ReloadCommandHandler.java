package io.tofpu.speedbridge2.reload.infra.command;

import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.command.CommandHandlerVisitor;
import io.tofpu.speedbridge2.reload.domain.ReloadRegistry;
import io.tofpu.speedbridge2.reload.domain.Reloadable;
import io.tofpu.speedbridge2.reload.service.ReloadService;

public class ReloadCommandHandler implements CommandHandlerVisitor {
    private final ReloadService reloadService;
    private final ReloadRegistry reloadRegistry;

    public ReloadCommandHandler(ReloadService reloadService, ReloadRegistry reloadRegistry) {
        this.reloadService = reloadService;
        this.reloadRegistry = reloadRegistry;
    }

    @Override
    public void visit(CommandHandler commandHandler) {
        commandHandler.modifyBuilder(builder -> builder.parameterTypes().addParameterType(Reloadable.Key.class, new ReloadableKeyParameterType(reloadRegistry)));
        commandHandler.addChildCommand(new ReloadCommand(reloadService));
    }

}
