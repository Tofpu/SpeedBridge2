package io.tofpu.speedbridge2.block.infra.config;

import io.tofpu.speedbridge2.block.domain.Block;
import io.tofpu.speedbridge2.block.domain.menu.BlockMenuSettings;
import io.tofpu.speedbridge2.block.domain.BlockSettings;
import io.tofpu.speedbridge2.block.domain.menu.MenuBlockRegistry;
import io.tofpu.speedbridge2.util.config.ConfigManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class BlockConfigManager {
    private final File configFolder;
    private ConfigManager<BlockConfiguration> configManager;

    public BlockConfigManager(File configFolder) {
        this.configFolder = configFolder;
    }

    public void load() {
        configManager = ConfigManager.create(configFolder.toPath(), "block.yml", BlockConfiguration.class);
        configManager.reloadConfig();
    }

    public BlockSettings blockConfiguration() {
        BlockConfiguration configuration = configurationOrThrow();
        return new BlockSettings(
                configuration.blockSlot(),
                configuration.defaultBlockId()
        );
    }

    public BlockMenuSettings blockMenuSettings() {
        BlockConfiguration configuration = configurationOrThrow();
        BlockConfiguration.Menu menu = configuration.menu();
        BlockConfiguration.Menu.Pattern pattern = menu.pattern();
        return new BlockMenuSettings(
                menu.title(),
                menu.rows(),
                new BlockMenuSettings.Pattern(
                        pattern.type(),
                        pattern.patterns(),
                        pattern.replacementItems()
                )
        );
    }

    public MenuBlockRegistry menuBlockRegistry() {
        BlockConfiguration configuration = configurationOrThrow();
        MenuBlockRegistry registry = new MenuBlockRegistry();
        configuration.blocks().forEach(block -> {
            Block domainBlock = new Block(
                    block.id(),
                    block.viewPermission(),
                    block.item()
            );
            registry.register(domainBlock);
        });
        return registry;
    }

    private @NotNull BlockConfiguration configurationOrThrow() {
        if (configManager == null) {
            throw new IllegalStateException("Block config manager has not been loaded yet");
        }
        return configManager.getConfigData();
    }
}
