package io.tofpu.speedbridge2.database.infra.config;

import io.tofpu.speedbridge2.database.infra.db.DriverType;
import io.tofpu.speedbridge2.database.infra.connection.ConnectionProvider;
import io.tofpu.speedbridge2.database.infra.connection.type.H2ConnectionProvider;
import io.tofpu.speedbridge2.util.config.ConfigManager;

import java.io.File;

public class ConnectionProviderLoader {
    private final ConfigManager<DatabaseConfiguration> configManager;
    private final File dataDirectory;

    public ConnectionProviderLoader(File configFile, File dataDirectory) {
        this.configManager = ConfigManager.create(
                configFile.getParentFile().toPath(),
                configFile.getName(),
                DatabaseConfiguration.class
        );
        this.dataDirectory = dataDirectory;
    }

    public ConnectionProvider loadConnectionProvider() {
        configManager.reloadConfig();

        DatabaseConfiguration config = configManager.getConfigData();
        DriverType driverType = config.driverType();
        DatabaseConfiguration.Drivers.Driver driver = config.selectedDriver();
        //noinspection SwitchStatementWithTooFewBranches
        switch (driverType) {
            case H2: {
                DatabaseConfiguration.Drivers.H2Driver h2Driver = (DatabaseConfiguration.Drivers.H2Driver) driver;
                if (h2Driver.type() == DatabaseConfiguration.Drivers.H2Driver.Type.MEMORY) {
                    return H2ConnectionProvider.memory();
                } else if (h2Driver.type() == DatabaseConfiguration.Drivers.H2Driver.Type.FILE) {
                    File file = new File(dataDirectory, h2Driver.fileName());
                    return H2ConnectionProvider.file(file);
                } else {
                    throw new IllegalStateException("Unsupported H2 driver type: " + h2Driver.type());
                }
            }
            default:
                throw new IllegalStateException("Unsupported driver type: " + driverType);
        }
    }
}
