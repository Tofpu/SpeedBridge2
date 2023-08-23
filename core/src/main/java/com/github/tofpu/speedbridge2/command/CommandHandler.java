package com.github.tofpu.speedbridge2.command;

import com.github.tofpu.speedbridge2.command.resolve.CommandResolver;
import com.github.tofpu.speedbridge2.command.resolve.argument.ArgumentResolver;
import com.github.tofpu.speedbridge2.command.mapper.CommandMapper;
import com.github.tofpu.speedbridge2.command.mapper.ObjectCommandMapper;
import com.github.tofpu.speedbridge2.command.resolve.SenderResolver;

import java.util.*;

public class CommandHandler {

    private final RegisteredCommandRegistry<CommandContainer> registry;
    private final CommandMapper<CommandContainer> commandMapper;
    private final SenderResolver senderResolver;
    private final ArgumentResolver argumentResolver;
    private final CommandInvoker<CommandContainer> commandInvoker;

    public CommandHandler() {
        this.registry = new RegisteredCommandRegistry<>();
        this.commandMapper = new ObjectCommandMapper();
        this.senderResolver = new SenderResolver();
        this.argumentResolver = new ArgumentResolver();
        CommandResolver<CommandContainer> commandResolver = new CommandResolver<>(registry);
        this.commandInvoker = new CommandInvoker<>(commandResolver, senderResolver, argumentResolver);

        registerSenders();
        registerResolvers();
    }

    protected void registerSenders() {}

    protected void registerResolvers() {
        this.argumentResolver.register(Integer.class, (context) -> {
            try {
                int parseInt = Integer.parseInt(context.peek());
                context.poll();
                return parseInt;
            } catch (NumberFormatException e) {
                return null;
            }
        });

        this.argumentResolver.register(Integer[].class, (context) -> {
            final Queue<Integer> array = new LinkedList<>();

            while (context.peek() != null) {
                Integer resolve = this.argumentResolver.resolve(Integer.class, context.original());
                if (resolve == null) {
                    break;
                }
                array.add(resolve);
                context.poll();
            }
            return array.toArray(new Integer[0]);
        });

        this.argumentResolver.register(String[].class, (context -> {
            final Queue<String> array = new LinkedList<>();

            while (context.peek() != null) {
                String resolve = context.poll();
                if (resolve == null) {
                    break;
                }
                array.add(resolve);
            }
            return array.toArray(new String[0]);
        }));
    }

    public void register(Object object) {
        CommandContainer command = this.commandMapper.map(object);
        registerToRegistry(command);
    }

    protected void registerToRegistry(CommandContainer command) {
        this.registry.register(command.name(), command);
    }

    public void invoke(UUID actorId, String command) {
        commandInvoker.invoke(actorId, command);
    }

    public void invoke(String command) {
        invoke(null, command);
    }

    public SenderResolver senderResolver() {
        return senderResolver;
    }
}