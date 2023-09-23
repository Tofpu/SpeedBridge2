package com.github.tofpu.speedbridge2.configuration.database.driver;

import com.github.tofpu.speedbridge2.database.driver.ConnectionType;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@SuppressWarnings("FieldMayBeFinal")
public class H2DriverConfiguration extends DriverConfiguration {
    private ConnectionType connectionType;

    public H2DriverConfiguration() {
        this(ConnectionType.MEMORY);
    }

    public H2DriverConfiguration(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public ConnectionType connectionType() {
        return connectionType;
    }
}
