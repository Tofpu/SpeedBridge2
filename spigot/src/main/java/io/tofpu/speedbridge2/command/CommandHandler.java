package io.tofpu.speedbridge2.command;

import io.tofpu.speedbridge2.util.ColorUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.orphan.Orphans;

public class CommandHandler {
    protected static final Orphans PARENT_ORPHAN_PATH = Orphans.path("speedbridge", "sb");
    private final Lamp.Builder<BukkitCommandActor> builder;
    private Lamp<BukkitCommandActor> lamp;

    private final Collection<ChildrenCommand> childrenCommands = new ArrayList<>();
    private boolean enabled = false;

    public CommandHandler(JavaPlugin plugin) {
        this.builder = BukkitLamp.builder(plugin)
                .defaultMessageSender((actor, message) -> actor.sendRawMessage(ColorUtil.colorize(message)));
    }

    public void modifyBuilder(Consumer<Lamp.Builder<BukkitCommandActor>> builderConsumer) {
        if (lamp != null) {
            throw new IllegalStateException(
                    "Lamp has been already initialized! The builder cannot be modified anymore.");
        }
        builderConsumer.accept(builder);
    }

    public void addChildCommand(ChildrenCommand command) {
        if (enabled) {
            throw new IllegalStateException(
                    "The CommandHandler has already been enabled. Commands must be added before CommandHandler is enabled.");
        }
        childrenCommands.add(command);
    }

    public void enable() {
        if (enabled) {
            throw new IllegalStateException("CommandHandler has already been enabled.");
        }
        lamp = builder.build();
        registerChildrenCommandsUnderParentCommand();
        enabled = true;
    }

    private void registerChildrenCommandsUnderParentCommand() {
        for (ChildrenCommand command : childrenCommands) {
            lamp.register(PARENT_ORPHAN_PATH.handler(command));
        }
        childrenCommands.clear();
    }
}
