package io.tofpu.speedbridge2.repository;

import io.tofpu.speedbridge2.repository.storage.BaseStorage;

import java.util.concurrent.CompletableFuture;

/**
 * A ready-to-use abstract class of {@link Repository} with {@link BaseStorage} included.
 *
 * @param <K> the unique key identifier of {@link O}
 * @param <O> the object to be inserted/fetched
 *
 * @see BaseStorage
 */
public abstract class BaseRepository<K, O> implements Repository<K, O> {
    protected final BaseStorage storage;

    protected BaseRepository(final BaseStorage storage) {
        this.storage = storage;
    }

    @Override
    public abstract CompletableFuture<O> fetch(final K key);

    @Override
    public abstract CompletableFuture<Boolean> isPresent(final K key);

    @Override
    public abstract CompletableFuture<Void> insert(final K key, final O obj);

    @Override
    public abstract void delete(final K key);
}
