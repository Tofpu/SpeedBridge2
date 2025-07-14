package io.tofpu.speedbridge2.group.domain.event;

import io.github.revxrsal.eventbus.gen.Index;
import io.tofpu.speedbridge2.group.domain.Group;

public interface GroupEvent {
    @Index(1)
    Group group();
}
