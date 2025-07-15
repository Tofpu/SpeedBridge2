package io.tofpu.speedbridge2.reload;

import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.reload.domain.ReloadRegistry;
import io.tofpu.speedbridge2.reload.infra.command.ReloadCommandHandler;
import io.tofpu.speedbridge2.reload.service.ReloadRegistryImpl;
import io.tofpu.speedbridge2.reload.service.ReloadService;

public class ReloadSystem {
    private final ReloadRegistry reloadRegistry = new ReloadRegistryImpl();
    private final ReloadService reloadService = new ReloadService(reloadRegistry);

    public void registerCommands(CommandHandler commandHandler) {
        commandHandler.accept(new ReloadCommandHandler(reloadService, reloadRegistry));
    }

    public ReloadService reloadService() {
        return reloadService;
    }

    public ReloadRegistry reloadRegistry() {
        return reloadRegistry;
    }
}
