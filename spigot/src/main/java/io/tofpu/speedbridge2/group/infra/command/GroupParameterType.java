package io.tofpu.speedbridge2.group.infra.command;

import io.tofpu.speedbridge2.group.domain.Group;
import io.tofpu.speedbridge2.group.service.GroupService;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

public class GroupParameterType implements ParameterType<BukkitCommandActor, Group> {
    private final GroupService groupService;

    public GroupParameterType(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public Group parse(@NotNull MutableStringStream input, @NotNull ExecutionContext<@NotNull BukkitCommandActor> context) {
        String groupName = input.readString();
        Group group = groupService.get(groupName);
        if (group == null) {
            throw new CommandErrorException("No such group: %s", groupName);
        }
        return group;
    }

    @Override
    public @NotNull SuggestionProvider<@NotNull BukkitCommandActor> defaultSuggestions() {
        return context -> groupService.groups().stream()
                .map(Group::name)
                .toList();
    }
}
