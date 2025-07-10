package io.tofpu.speedbridge2.block.domain;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface BlockRepository {
    CompletableFuture<String> find(UUID playerId);
    CompletableFuture<Void> save(UUID playerId, String blockId);
    CompletableFuture<Void> delete(UUID playerId);
}
