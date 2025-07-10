package io.tofpu.speedbridge2.block.system;

import io.github.revxrsal.eventbus.EventBus;
import io.tofpu.speedbridge2.block.domain.Block;
import io.tofpu.speedbridge2.block.domain.menu.MenuBlockRegistry;
import io.tofpu.speedbridge2.block.infra.command.BlockCommand;
import io.tofpu.speedbridge2.block.infra.config.BlockConfigManager;
import io.tofpu.speedbridge2.block.infra.listener.GameStateListener;
import io.tofpu.speedbridge2.block.infra.listener.PlayerConnectionListener;
import io.tofpu.speedbridge2.block.service.BlockMenuService;
import io.tofpu.speedbridge2.block.persistance.BlockDao;
import io.tofpu.speedbridge2.block.persistance.BlockRepositoryImpl;
import io.tofpu.speedbridge2.block.service.BlockService;
import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.database.infra.db.Database;
import io.tofpu.speedbridge2.util.listener.ListenerRegistration;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

import java.io.File;
import java.util.Objects;

public class BlockSystem {
    private BlockService blockService;
    private BlockMenuService blockMenuService;

    public void load(Database database, File configFolder) {
        BlockConfigManager configManager = new BlockConfigManager(configFolder);
        configManager.load();

        MenuBlockRegistry menuBlockRegistry = configManager.menuBlockRegistry();
        this.blockService = new BlockService(
                new BlockRepositoryImpl(new BlockDao(database)),
                configManager.blockConfiguration(),
                menuBlockRegistry
        );
        this.blockMenuService = new BlockMenuService(
                blockService,
                configManager.blockMenuSettings(),
                menuBlockRegistry
        );
    }

    public void registerListeners(ListenerRegistration listenerRegistration, EventBus eventBus) {
        Objects.requireNonNull(listenerRegistration, "Listener registration cannot be null");
        if (blockService == null) {
            throw new IllegalStateException("Block service has not been loaded yet. Call load() first.");
        }
        listenerRegistration.register(new PlayerConnectionListener(blockService));
        eventBus.register(new GameStateListener(blockService));

        this.blockMenuService.registerListener(listenerRegistration);
    }

    public void registerCommands(CommandHandler handler) {
        Objects.requireNonNull(handler, "Command handler cannot be null");
        if (blockService == null) {
            throw new IllegalStateException("Block service has not been loaded yet. Call load() first.");
        }
        handler.modifyBuilder(builder -> {
            builder.parameterTypes()
                    .addParameterType(Block.class, new ParameterType<>() {
                        @Override
                        public Block parse(@NotNull MutableStringStream input, @NotNull ExecutionContext<@NotNull BukkitCommandActor> context) {
                            String blockId = input.readString();
                            return blockService.block(blockId);
                        }

                        @Override
                        public @NotNull SuggestionProvider<@NotNull BukkitCommandActor> defaultSuggestions() {
                            return context -> blockService.blockIds();
                        }
                    });
        });
        handler.addChildCommand(new BlockCommand(blockService, blockMenuService));
    }

    public BlockService blockService() {
        if (blockService == null) {
            throw new IllegalStateException("Block service has not been loaded yet. Call load() first.");
        }
        return blockService;
    }
}
