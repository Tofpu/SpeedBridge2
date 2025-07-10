package io.tofpu.speedbridge2.block.domain.menu;

import io.tofpu.speedbridge2.block.domain.Block;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MenuBlockRegistry {
    private final Map<String, Block> blockMap = new HashMap<>();

    public void register(Block block) {
        if (blockMap.containsKey(block.id())) {
            throw new IllegalStateException("Block with id " + block.id() + " is already registered.");
        }
        blockMap.put(block.id(), block);
    }

    public Optional<Block> get(String id) {
        return Optional.ofNullable(blockMap.get(id));
    }

    public Block getOrThrow(String id) {
        Block block = blockMap.get(id);
        if (block == null) {
            throw new IllegalStateException("No block found with id " + id);
        }
        return block;
    }

    public Collection<Block> all() {
        return blockMap.values();
    }
}
