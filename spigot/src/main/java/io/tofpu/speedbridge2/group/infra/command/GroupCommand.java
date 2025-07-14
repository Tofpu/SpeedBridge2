package io.tofpu.speedbridge2.group.infra.command;

import io.tofpu.speedbridge2.command.ChildrenCommand;
import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.command.CommandHandlerVisitor;
import io.tofpu.speedbridge2.group.domain.Group;
import io.tofpu.speedbridge2.group.service.GroupService;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.command.CommandActor;

import java.util.Collection;
import java.util.UUID;

@Subcommand("group")
public class GroupCommand extends ChildrenCommand implements CommandHandlerVisitor {
    private final GroupService service;

    public GroupCommand(GroupService service) {
        this.service = service;
    }

    @Subcommand("create")
    public void addGroup(CommandActor sender, String groupName, @Optional UUID groupId) {
        if (service.contains(groupName)) {
            sender.reply("There's already a group with name '%s'!".formatted(groupName));
            return;
        }
        if (groupId != null && service.contains(groupId)) {
            sender.reply("There's already a group with id '%s': '%s'".formatted(groupId, service.getById(groupId).name()));
            return;
        }

        sender.reply("&eCreating group named '%s' now...".formatted(groupName));
        service.createGroup(groupName, groupId)
                .thenAccept(group -> {
                    if (group != null) {
                        sender.reply("&aCreated group '%s' successfully.".formatted(group.name()));
                    } else {
                        sender.reply("&cFailed to create group '%s'.".formatted(groupName));
                    }
                });
    }

    @Subcommand("remove")
    public void removeGroup(CommandActor sender, Group group) {
        sender.reply("&eRemoving group '%s' (%s) now...".formatted(group.name(), group.id()));
        service.remove(group)
                .thenAccept(result -> {
                    if (result) {
                        sender.reply("&aRemoved group '%s' (%s) successfully!".formatted(group.name(), group.name()));
                    } else {
                        sender.reply("&cFailed to remove group '%s' (%s)".formatted(group.name(), group.id()));
                    }
                });
    }

    @Subcommand("list")
    public void groupList(CommandActor sender) {
        Collection<Group> groups = service.groups();
        if (groups.isEmpty()) {
            sender.reply("&cNo registered groups found.");
            return;
        }

        sender.reply("&7List of registered groups:");
        int number = 0;
        groups.forEach(group -> {
            String format = "%d) &7'%s' &8(id %s)";
            sender.reply(format.formatted(number, group.name(), group.id()));
        });
    }

    @Override
    public void visit(CommandHandler commandHandler) {
        commandHandler.modifyBuilder(builder -> builder.parameterTypes().addParameterType(Group.class, new GroupParameterType(service)));
        commandHandler.addChildCommand(this);
    }
}
