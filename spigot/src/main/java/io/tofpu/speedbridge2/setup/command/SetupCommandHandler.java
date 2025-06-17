package io.tofpu.speedbridge2.setup.command;

import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.schematic.domain.Schematic;
import io.tofpu.speedbridge2.schematic.infra.SchematicHandler;
import io.tofpu.speedbridge2.setup.service.SetupService;

public class SetupCommandHandler {
    private final CommandHandler commandHandler;
    private final SchematicHandler schematicHandler;
    private final SetupService setupService;

    public static void init(
            CommandHandler commandHandler, SchematicHandler schematicHandler, SetupService setupService) {
        SetupCommandHandler handler = new SetupCommandHandler(commandHandler, schematicHandler, setupService);
        handler.register();
    }

    public SetupCommandHandler(
            CommandHandler commandHandler, SchematicHandler schematicHandler, SetupService setupService) {
        this.commandHandler = commandHandler;
        this.schematicHandler = schematicHandler;
        this.setupService = setupService;
    }

    public void register() {
        commandHandler.modifyBuilder(lampBuilder -> lampBuilder.parameterTypes(builder -> {
            builder.addParameterType(Schematic.class, new SchematicParameterType(schematicHandler));
        }));
        commandHandler.addChildCommand(new SetupCommand(setupService));
    }
}
