package com.github.tofpu.speedbridge2.bukkit.command;

import com.github.tofpu.speedbridge2.bukkit.command.util.CommandUtil;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.command.CommandCategory;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.core.CommandPath;
import revxrsal.commands.help.CommandHelp;
import revxrsal.commands.help.CommandHelpWriter;
import revxrsal.commands.process.ContextResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * Aggregates direct related command entries only.
 *
 * @param <T> type must match with {@link revxrsal.commands.help.CommandHelpWriter<T>}
 * @see CommandHelp
 */
public interface RelatedCommandHelp<T> extends List<T> {
    class Resolver implements ContextResolver<RelatedCommandHelp<?>> {
        private final CommandHandler handler;

        public Resolver(CommandHandler handler) {
            this.handler = handler;
        }

        @Override
        public RelatedCommandHelp<?> resolve(@NotNull ContextResolver.ContextResolverContext context) {
            if (handler.getHelpWriter() == null)
                throw new IllegalArgumentException("No help writer is registered!");

            ExecutableCommand helpCommand = context.command();
            CommandHelpWriter<?> writer = handler.getHelpWriter();
            BaseRelatedCommandHelp<Object> entries = new BaseRelatedCommandHelp<>();
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
                if (!CommandUtil.isSameRoot(fullCommandPath, commandPath)) {
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

        static final class BaseRelatedCommandHelp<T> extends ArrayList<T> implements RelatedCommandHelp<T> {}
    }
}
