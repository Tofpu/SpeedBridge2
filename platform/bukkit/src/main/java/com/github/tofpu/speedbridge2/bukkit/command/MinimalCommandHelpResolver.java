package com.github.tofpu.speedbridge2.bukkit.command;

import org.jetbrains.annotations.NotNull;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.command.CommandCategory;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.core.CommandPath;
import revxrsal.commands.help.CommandHelpWriter;
import revxrsal.commands.process.ContextResolver;

import java.util.ArrayList;
import java.util.Objects;

class MinimalCommandHelpResolver implements ContextResolver<MinimalCommandHelp<?>> {
    private final CommandHandler handler;

    public MinimalCommandHelpResolver(CommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public MinimalCommandHelp<?> resolve(@NotNull ContextResolver.ContextResolverContext context) {
        if (handler.getHelpWriter() == null)
            throw new IllegalArgumentException("No help writer is registered!");

        ExecutableCommand helpCommand = context.command();
        CommandHelpWriter<?> writer = handler.getHelpWriter();
        BaseMinimalCommandHelp<Object> entries = new BaseMinimalCommandHelp<>();
        CommandCategory parent = helpCommand.getParent();
        CommandPath fullCommandPath = helpCommand.getPath();
        CommandPath parentPath = parent == null ? null : parent.getPath();

        if (parentPath == null) {
            return entries;
        }

        CommandCategory commandCategory = handler.getCategory(parentPath);
        if (commandCategory == null) {
            return entries;
        }

        handler.getCommands().forEach((commandPath, executableCommand) -> {
            System.out.println(parent.getName() + "(" + parent + ")" + " vs " + commandPath.getLast() + "(" + commandPath + ")");

            if (!Objects.equals(fullCommandPath.getFirst(), commandPath.getFirst())) {
                System.out.println("first: " + fullCommandPath.getFirst() + " != " + commandPath.getFirst());
                return;
            }

            if (!commandPath.isChildOf(parentPath)) {
                System.out.println("different child");
                return;
            }

            entries.add(writer.generate(executableCommand, context.actor()));
        });
        return entries;
    }

    static final class BaseMinimalCommandHelp<T> extends ArrayList<T> implements MinimalCommandHelp<T> {}
}
