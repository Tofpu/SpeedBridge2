package io.tofpu.speedbridge2.setup.command;

import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.schematic.Schematic;
import io.tofpu.speedbridge2.schematic.SchematicService;
import io.tofpu.speedbridge2.setup.service.SetupService;

public class SetupCommandHandler {
    private final CommandHandler commandHandler;
    private final SchematicService schematicService;
    private final SetupService setupService;

    public SetupCommandHandler(
            CommandHandler commandHandler, SchematicService schematicService, SetupService setupService) {
        this.commandHandler = commandHandler;
        this.schematicService = schematicService;
        this.setupService = setupService;
    }

    public void register() {
        commandHandler.modifyBuilder(lampBuilder -> lampBuilder.parameterTypes(builder -> {
            builder.addParameterType(Schematic.class, new SchematicParameterType(schematicService));
        }));
        commandHandler.addChildCommand(new SetupCommand(setupService));
    }
}
