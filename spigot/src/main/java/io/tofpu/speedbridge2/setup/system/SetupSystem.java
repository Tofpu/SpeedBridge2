package io.tofpu.speedbridge2.setup.system;

import io.github.revxrsal.eventbus.EventBus;
import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.island.service.IslandService;
import io.tofpu.speedbridge2.lobby.LobbyTeleporter;
import io.tofpu.speedbridge2.schematic.infra.SchematicHandler;
import io.tofpu.speedbridge2.setup.command.SetupCommandHandler;
import io.tofpu.speedbridge2.setup.infra.listener.equipment.SetupToolsHandler;
import io.tofpu.speedbridge2.setup.infra.listener.indication.VirtualSetupBorder;
import io.tofpu.speedbridge2.setup.service.SetupService;
import io.tofpu.toolbar.ToolbarAPI;
import org.bukkit.World;

public class SetupSystem {
    private final EventBus eventBus;
    private final SetupService service;

    public SetupSystem(EventBus eventBus, IslandService islandService, LobbyTeleporter lobbyTeleporter, World arenaWorld) {
        this.eventBus = eventBus;
        this.service = new SetupService(
                eventBus, islandService,
                lobbyTeleporter, arenaWorld
        );
    }

    public void registerListeners(ToolbarAPI toolbarAPI) {
        SetupToolsHandler setupToolsHandler = new SetupToolsHandler(toolbarAPI, service);
        setupToolsHandler.registerToolbar();
        eventBus.register(setupToolsHandler);

        eventBus.register(new VirtualSetupBorder());
    }

    public void registerCommand(CommandHandler handler, SchematicHandler schematicHandler) {
        SetupCommandHandler.init(handler, schematicHandler, service);
    }
}
