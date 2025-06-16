package io.tofpu.speedbridge2.setup.system;

import io.github.revxrsal.eventbus.EventBus;
import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.island.service.IslandService;
import io.tofpu.speedbridge2.lobby.LobbyTeleporter;
import io.tofpu.speedbridge2.schematic.domain.Schematic;
import io.tofpu.speedbridge2.schematic.infra.SchematicHandler;
import io.tofpu.speedbridge2.setup.service.SetupService;
import io.tofpu.speedbridge2.setup.command.SetupCommand;
import org.bukkit.World;
import revxrsal.commands.exception.CommandErrorException;

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

    public void registerCommand(CommandHandler handler, SchematicHandler schematicHandler) {
        handler.modifyBuilder(builder -> builder.parameterTypes(paramBuilder -> {
            paramBuilder.addParameterType(Schematic.class, (stream, executionContext) -> {
                String schematicName = stream.readString();
                Schematic schematic = schematicHandler.resolveSchematic(schematicName);
                if (schematic == null) {
                    throw new CommandErrorException("Schematic not found: " + schematicName);
                }
                return schematic;
            });
        }));
        handler.addChildCommand(new SetupCommand(service));
    }
}
