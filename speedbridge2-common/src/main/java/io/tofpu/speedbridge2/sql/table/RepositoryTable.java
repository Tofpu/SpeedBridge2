package io.tofpu.speedbridge2.sql.table;

/**
 * An interface that stores table's name and its parameters.
 * @see SQLTableUtil
 */
public interface RepositoryTable {
    String getTableName();
    String[] getTableParameters();
}
