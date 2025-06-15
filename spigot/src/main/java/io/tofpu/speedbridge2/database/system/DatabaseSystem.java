package io.tofpu.speedbridge2.database.system;

import io.tofpu.speedbridge2.database.infra.config.ConnectionProviderLoader;
import io.tofpu.speedbridge2.database.infra.db.Database;

import java.io.File;

public class DatabaseSystem {
    private final File configFile;
    private final File dataDirectory;

    private Database database;

    public DatabaseSystem(File configFile, File dataDirectory) {
        this.configFile = configFile;
        this.dataDirectory = dataDirectory;
    }

    public void load() {
        ConnectionProviderLoader loader = new ConnectionProviderLoader(configFile, dataDirectory);
        this.database = new Database(loader.loadConnectionProvider());
    }

    public Database database() {
        if (database == null) {
            throw new IllegalStateException("Database has not been loaded yet. Call load() first.");
        }
        return database;
    }
}
