package io.tofpu.speedbridge2.group;

import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.database.infra.db.Database;
import io.tofpu.speedbridge2.group.infra.command.GroupCommand;
import io.tofpu.speedbridge2.group.persistance.GroupDao;
import io.tofpu.speedbridge2.group.persistance.GroupMapper;
import io.tofpu.speedbridge2.group.persistance.GroupRepositoryImpl;
import io.tofpu.speedbridge2.group.service.GroupService;

// todo: couple of foundmental issues and fixable bugs:
//  1. island associates with a group by their id
//    - e.g. let's say an island was associated with group 'easy' (id 0), if we removed it and registered  group 'hard',
//    it'll be assigned with id 0 as well. This could cause unexpected consequences.
public class GroupSystem {
    private GroupService groupService;

    public void initialize(Database database) {
        GroupRepositoryImpl repository = new GroupRepositoryImpl(new GroupDao(database), new GroupMapper());
        this.groupService = new GroupService(repository);
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
