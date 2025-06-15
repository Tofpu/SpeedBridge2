package io.tofpu.speedbridge2.island.command;

import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.island.domain.Island;
import io.tofpu.speedbridge2.island.service.IslandService;
import io.tofpu.speedbridge2.island.command.parameter.IslandParameterType;

public class IslandCommandHandler {
    private final CommandHandler commandHandler;
    private final IslandService islandService;

    public static void init(CommandHandler commandHandler, IslandService islandService) {
        new IslandCommandHandler(commandHandler, islandService).register();
    }

    private IslandCommandHandler(CommandHandler commandHandler, IslandService islandService) {
        this.commandHandler = commandHandler;
        this.islandService = islandService;
    }

    public void register() {
        commandHandler.modifyBuilder(lampBuilder -> {
            lampBuilder.parameterTypes(builder -> {
                builder.addParameterType(Island.class, new IslandParameterType(islandService));
            });
        });
        commandHandler.addChildCommand(new IslandCommand(islandService));
    }
}
