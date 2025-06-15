package io.tofpu.speedbridge2.database.infra.config;

import io.tofpu.speedbridge2.database.infra.db.DriverType;
import org.immutables.value.Value;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.SubSection;

public interface DatabaseConfiguration {
    @ConfDefault.DefaultString("H2")
    DriverType driverType();

    @SubSection
    Drivers drivers();

    default Drivers.Driver selectedDriver() {
        //noinspection SwitchStatementWithTooFewBranches,EnhancedSwitchMigration
        switch (driverType()) {
            case H2:
                return drivers().h2();
            default:
                throw new IllegalStateException("Unsupported driver type: " + driverType());
        }
    }

    @Value.Immutable
    interface Drivers {
        @SubSection
        H2Driver h2();

        interface Driver {
        }

        @Value.Immutable
        interface H2Driver extends Driver {
            @ConfDefault.DefaultString("MEMORY")
            Type type();

            @ConfDefault.DefaultString("storage.db")
            String fileName();

            enum Type {
                MEMORY, FILE
            }

            static H2Driver of(Type type, String fileName) {
                return ImmutableH2Driver.of(type, fileName);
            }

            @SuppressWarnings("unused")
            static H2Driver.Builder newBuilder() {
                return new Builder();
            }

            class Builder extends ImmutableH2Driver.Builder {
            }
        }

        @SuppressWarnings("unused")
        static Builder newBuilder() {
            return new Builder();
        }

        class Builder extends ImmutableDrivers.Builder {
        }

        static Drivers of(H2Driver h2) {
            return ImmutableDrivers.of(h2);
        }
    }
}
