package io.tofpu.speedbridge2.repository;

import io.tofpu.speedbridge2.repository.storage.BaseStorage;
import io.tofpu.speedbridge2.sql.table.RepositoryTable;

/**
 * A regular {@link BaseRepository} class with table details.
 * <p>
 * {@link TableBaseRepository} is useful for situations where you have
 * multiple repositories with distinct tables.
 *
 * @param <K> the unique key identifier of {@link O}
 * @param <O> the object to be inserted/fetched
 *
 * @see BaseRepository
 */
public abstract class TableBaseRepository<K, O> extends BaseRepository<K, O> {
    protected TableBaseRepository(final BaseStorage storage) {
        super(storage);
    }

    /**
     * @return the repository's table
     */
    public abstract RepositoryTable getTable();
}
