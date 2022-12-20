package io.tofpu.speedbridge2.repository.storage;

import java.sql.Connection;

/**
 * An interface responsible for holding
 * SQL storage-related information.
 */
public interface SQLStorage {
    Connection getConnection();
    void establishConnection();
}
