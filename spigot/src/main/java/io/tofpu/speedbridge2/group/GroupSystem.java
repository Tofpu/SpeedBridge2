package io.tofpu.speedbridge2.group;

import io.github.revxrsal.eventbus.EventBus;
import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.database.infra.db.Database;
import io.tofpu.speedbridge2.group.infra.command.GroupCommand;
import io.tofpu.speedbridge2.group.persistance.GroupDao;
import io.tofpu.speedbridge2.group.persistance.GroupMapper;
import io.tofpu.speedbridge2.group.persistance.GroupRepositoryImpl;
import io.tofpu.speedbridge2.group.service.GroupService;

public class GroupSystem {
    private final EventBus eventBus;
    private GroupService groupService;

    public GroupSystem(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void initialize(Database database) {
        GroupRepositoryImpl repository = new GroupRepositoryImpl(new GroupDao(database), new GroupMapper());
        this.groupService = new GroupService(repository, eventBus);
        this.groupService.load();
    }

    public void registerCommands(CommandHandler handler) {
        ensureGroupServiceIsPresent();
        GroupCommand command = new GroupCommand(groupService);
        handler.accept(command);
    }

    private void ensureGroupServiceIsPresent() {
        if (groupService == null) {
            throw new IllegalStateException("GroupService is not initialized. call #initialize first.");
        }
    }

    public GroupService groupService() {
        return groupService;
    }
}
