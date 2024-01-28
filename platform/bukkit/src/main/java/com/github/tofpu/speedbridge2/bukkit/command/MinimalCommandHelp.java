package com.github.tofpu.speedbridge2.bukkit.command;

import com.github.tofpu.speedbridge2.bukkit.command.util.CommandUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.command.CommandCategory;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.core.CommandPath;
import revxrsal.commands.exception.InvalidHelpPageException;
import revxrsal.commands.help.CommandHelp;
import revxrsal.commands.help.CommandHelpWriter;
import revxrsal.commands.process.ContextResolver;

import java.util.*;

/**
 * Sort the command help entries by the smallest path to the largest.
 *
 * @param <T> type must match with {@link revxrsal.commands.help.CommandHelpWriter<T>}
 * @see CommandHelp
 */
public interface MinimalCommandHelp<T> extends CommandHelp<T> {
    class Resolver implements ContextResolver<MinimalCommandHelp<?>> {
        private final CommandHandler handler;

        public Resolver(CommandHandler handler) {
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

            LinkedList<CommandPath> qualifiedPaths = new LinkedList<>();
            Map<CommandPath, ExecutableCommand> pathExecutableCommandMap = new HashMap<>();

            handler.getCommands().forEach((commandPath, executableCommand) -> {
                if (!CommandUtil.isSameRoot(fullCommandPath, commandPath)) {
                    System.out.println("first: " + fullCommandPath.getFirst() + " != " + commandPath.getFirst());
                    return;
                }
                qualifiedPaths.add(commandPath);
                pathExecutableCommandMap.put(commandPath, executableCommand);
            });

            qualifiedPaths.sort(Comparator.comparingInt(CommandPath::size));
            qualifiedPaths.forEach(commandPath -> entries.add(writer.generate(pathExecutableCommandMap.get(commandPath), context.actor())));
            return entries;
        }

        static final class BaseMinimalCommandHelp<T> extends ArrayList<T> implements MinimalCommandHelp<T> {
            @Override
            public MinimalCommandHelp<T> paginate(int page, int elementsPerPage) throws InvalidHelpPageException {
                if (isEmpty()) return new BaseMinimalCommandHelp<>();
                BaseMinimalCommandHelp<T> list = new BaseMinimalCommandHelp<>();
                int size = getPageSize(elementsPerPage);
                if (page > size)
                    throw new InvalidHelpPageException(this, page, elementsPerPage);
                int listIndex = page - 1;
                int l = Math.min(page * elementsPerPage, size());
                for (int i = listIndex * elementsPerPage; i < l; ++i) {
                    list.add(get(i));
                }
                return list;
            }

            @Override
            public @Range(from = 1, to = Long.MAX_VALUE) int getPageSize(int elementsPerPage) {
                if (elementsPerPage < 1)
                    throw new IllegalArgumentException("Elements per page cannot be less than 1! (Found " + elementsPerPage + ")");
                return (size() / elementsPerPage) + (size() % elementsPerPage == 0 ? 0 : 1);
            }
        }
    }
}
