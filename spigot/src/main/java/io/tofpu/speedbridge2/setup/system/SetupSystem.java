package io.tofpu.speedbridge2.setup.system;

import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.island.service.IslandService;
import io.tofpu.speedbridge2.lobby.LobbyTeleporter;
import io.tofpu.speedbridge2.schematic.Schematic;
import io.tofpu.speedbridge2.schematic.SchematicService;
import io.tofpu.speedbridge2.setup.service.SetupService;
import io.tofpu.speedbridge2.setup.command.SetupCommand;
import org.bukkit.World;
import revxrsal.commands.exception.CommandErrorException;

public class SetupSystem {
    private final SetupService service;

    public SetupSystem(IslandService islandService, LobbyTeleporter lobbyTeleporter, World arenaWorld) {
        this.service = new SetupService(
                islandService, lobbyTeleporter, arenaWorld
        );
    }

    public void registerCommand(CommandHandler handler, SchematicService schematicService) {
        handler.modifyBuilder(builder -> builder.parameterTypes(paramBuilder -> {
            paramBuilder.addParameterType(Schematic.class, (stream, executionContext) -> {
                String schematicName = stream.readString();
                Schematic schematic = schematicService.resolveSchematic(schematicName);
                if (schematic == null) {
                    throw new CommandErrorException("Schematic not found: " + schematicName);
                }
                return schematic;
            });
        }));
        handler.addChildCommand(new SetupCommand(service));
    }
}
