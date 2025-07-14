package io.tofpu.speedbridge2.island.system;

import io.github.revxrsal.eventbus.EventBus;
import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.database.infra.db.Database;
import io.tofpu.speedbridge2.group.service.GroupService;
import io.tofpu.speedbridge2.island.command.IslandCommandHandler;
import io.tofpu.speedbridge2.island.domain.IslandRepository;
import io.tofpu.speedbridge2.island.infra.group.GroupRegistrationListener;
import io.tofpu.speedbridge2.island.persistence.IslandDao;
import io.tofpu.speedbridge2.island.persistence.IslandMapper;
import io.tofpu.speedbridge2.island.persistence.IslandRepositoryImpl;
import io.tofpu.speedbridge2.island.service.IslandService;
import io.tofpu.speedbridge2.schematic.infra.SchematicHandler;

public class IslandSystem {
    private IslandService islandService;
    private SchematicHandler schematicHandler;
    private GroupService groupService;

    public void loadData(Database database, SchematicHandler schematicHandler, GroupService groupService) {
        this.schematicHandler = schematicHandler;
        this.groupService = groupService;
        IslandRepository repository = new IslandRepositoryImpl(
                new IslandDao(database),
                new IslandMapper(),
                schematicHandler,
                groupService
        );
        islandService = new IslandService(repository);
        islandService.load();
    }

    public void registerCommands(CommandHandler commandHandler) {
        if (islandService == null) {
            throw new IllegalStateException("IslandService is not initialized. Call load() first.");
        }
        IslandCommandHandler.init(commandHandler, islandService);
    }

    public void registerListeners(EventBus eventBus) {
        GroupRegistrationListener groupRegistrationListener = new GroupRegistrationListener(islandService, groupService, schematicHandler);
        eventBus.register(groupRegistrationListener);
    }

    public IslandService islandService() {
        if (islandService == null) {
            throw new IllegalStateException("IslandService is not initialized. Call enable() first.");
        }
        return islandService;
    }
}
