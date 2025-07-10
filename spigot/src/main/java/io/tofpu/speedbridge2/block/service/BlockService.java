package io.tofpu.speedbridge2.block.service;

import io.tofpu.speedbridge2.block.domain.Block;
import io.tofpu.speedbridge2.block.domain.BlockRepository;
import io.tofpu.speedbridge2.block.domain.BlockSettings;
import io.tofpu.speedbridge2.block.domain.menu.MenuBlockRegistry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BlockService {
    private final BlockRepository repository;
    private final BlockSettings blockSettings;
    private final MenuBlockRegistry menuBlockRegistry;

    private final Map<UUID, Block> blockCache = new HashMap<>();

    public BlockService(BlockRepository repository, BlockSettings blockSettings, MenuBlockRegistry menuBlockRegistry) {
        this.repository = repository;
        this.blockSettings = blockSettings;
        this.menuBlockRegistry = menuBlockRegistry;
    }

    public CompletableFuture<ItemStack> loadBlock(UUID playerId) {
        return repository.find(playerId)
                .thenApply(this::blockFromMenuRegistry).thenApply(block -> {
                    blockCache.put(playerId, block);
                    return block.item();
                });
    }

    public void spawnBlock(Player player) {
        UUID playerId = player.getUniqueId();
        ItemStack blockItem = getOrDefaultItem(playerId).asQuantity(64);
        player.getInventory().setItem(blockSettings.slot(), blockItem);
    }

    private ItemStack getOrDefaultItem(UUID playerId) {
        Block block = blockCache.get(playerId);
        if (block == null) {
            block = blockFromMenuRegistry(null); // Default block if not found
        }
        return block.item();
    }

    public CompletableFuture<Void> setBlock(UUID playerId, String blockId) {
        if (blockId == null || blockId.isEmpty()) {
            throw new IllegalArgumentException("Block ID cannot be null or empty");
        }
        Block block = menuBlockRegistry.getOrThrow(blockId);
        blockCache.put(playerId, block);
        return repository.save(playerId, block.id());
    }

    public void invalidate(UUID playerId) {
        blockCache.remove(playerId);
    }

    private Block blockFromMenuRegistry(String blockId) {
        if (blockId == null || blockId.isEmpty()) {
            // If no blockId is provided, use the default block ID from settings
            blockId = blockSettings.defaultBlockId();
        }
        return menuBlockRegistry.getOrThrow(blockId);
    }

    public String blockId(UUID playerId) {
        Block block = blockCache.get(playerId);
        if (block == null) {
            return null;
        }
        return block.id();
    }

    public Block block(String blockId) {
        return blockFromMenuRegistry(blockId);
    }

    public @NotNull Collection<String> blockIds() {
        return menuBlockRegistry.all()
                .stream()
                .map(Block::id)
                .toList();
    }
}
