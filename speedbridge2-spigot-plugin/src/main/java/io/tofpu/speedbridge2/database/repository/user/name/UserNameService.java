package io.tofpu.speedbridge2.database.repository.user.name;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface UserNameService {
    CompletableFuture<String> fetchName(UUID key);

    CompletableFuture<Boolean> isNamePresent(UUID key);

    CompletableFuture<Void> insertName(UUID key, String obj);

    CompletableFuture<Void> deleteName(UUID key);
}
