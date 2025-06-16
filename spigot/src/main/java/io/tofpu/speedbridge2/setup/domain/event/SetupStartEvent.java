package io.tofpu.speedbridge2.setup.domain.event;

import io.github.revxrsal.eventbus.gen.Index;
import io.tofpu.speedbridge2.setup.service.IslandSetup;

/**
 * Event that is fired when an island setup starts.
 * This event can be used to handle the beginning of an island setup process,
 * allowing for actions to be taken when a setup is initiated.
 */
public interface SetupStartEvent {
    @Index(0)
    IslandSetup getSetup();
}
