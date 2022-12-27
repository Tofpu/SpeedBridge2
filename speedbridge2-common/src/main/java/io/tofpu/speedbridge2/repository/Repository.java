package io.tofpu.speedbridge2.repository;

import java.util.concurrent.CompletableFuture;

/**
 * An interface for repositories.
 *
 * @param <K> the unique key identifier of {@link O}
 * @param <O> the object to be inserted/fetched
 *
 * @see BaseRepository
 */
public interface Repository<K, O> {
    CompletableFuture<O> fetch(final K key);
    CompletableFuture<Boolean> isPresent(final K key);

    CompletableFuture<Void> insert(final K key, final O obj);
    CompletableFuture<Void> delete(final K key);
}
