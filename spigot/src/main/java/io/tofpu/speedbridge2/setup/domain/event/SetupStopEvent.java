package io.tofpu.speedbridge2.setup.domain.event;

import io.github.revxrsal.eventbus.gen.Index;
import io.github.revxrsal.eventbus.gen.Property;
import io.tofpu.speedbridge2.setup.service.IslandSetup;

/**
 * Event that is fired when an island setup stops.
 * This event can be used to handle the end of an island setup process,
 * allowing for actions to be taken based on whether the setup was successful or cancelled.
 */
public interface SetupStopEvent {
    @Index(0) @Property
    IslandSetup getSetup();
    @Index(1) @Property
    Type getType();

    enum Type {
        SUCCESS,
        CANCELLED
    }
}
