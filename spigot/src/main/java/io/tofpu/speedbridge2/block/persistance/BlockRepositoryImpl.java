package io.tofpu.speedbridge2.block.persistance;

import io.tofpu.speedbridge2.block.domain.BlockRepository;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BlockRepositoryImpl implements BlockRepository {
    private final BlockDao blockDao;

    public BlockRepositoryImpl(BlockDao blockDao) {
        this.blockDao = blockDao;
    }

    @Override
    public CompletableFuture<String> find(UUID playerId) {
        Objects.requireNonNull(playerId, "Player ID cannot be null");
        if (playerId.toString().isEmpty()) {
            throw new IllegalArgumentException("Player ID cannot be empty");
        }
        return blockDao.find(playerId);
    }

    @Override
    public CompletableFuture<Void> save(UUID playerId, String blockId) {
        Objects.requireNonNull(playerId, "Player ID cannot be null");
        if (playerId.toString().isEmpty()) {
            throw new IllegalArgumentException("Player ID cannot be empty");
        }
        if (blockId == null || blockId.isEmpty()) {
            throw new IllegalArgumentException("Block ID cannot be null or empty");
        }
        return blockDao.save(playerId, blockId);
    }

    @Override
    public CompletableFuture<Void> delete(UUID playerId) {
        Objects.requireNonNull(playerId, "Player ID cannot be null");
        if (playerId.toString().isEmpty()) {
            throw new IllegalArgumentException("Player ID cannot be empty");
        }
        return blockDao.delete(playerId);
    }
}
