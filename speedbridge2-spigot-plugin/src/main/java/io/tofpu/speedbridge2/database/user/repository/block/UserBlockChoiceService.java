package io.tofpu.speedbridge2.database.user.repository.block;

import org.bukkit.Material;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface UserBlockChoiceService {
    CompletableFuture<Void> insertBlockChoice(UUID key, Material value);
    CompletableFuture<Material> fetchBlockChoice(UUID key);
    CompletableFuture<Boolean> isBlockChoicePresent(UUID key);
    CompletableFuture<Void> deleteBlockChoice(UUID key);
}
