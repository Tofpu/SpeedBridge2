package io.tofpu.speedbridge2.repository.storage;

import java.util.concurrent.CompletableFuture;

/**
 * A {@link SQLStorage} interface with additional initialization responsibility.
 *
 * @see SQLStorage
 */
public interface ParentStorage extends SQLStorage {
    CompletableFuture<Void> init();
}
