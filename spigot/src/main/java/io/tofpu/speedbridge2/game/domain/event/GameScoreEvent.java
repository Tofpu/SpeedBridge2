package io.tofpu.speedbridge2.game.domain.event;

import io.github.revxrsal.eventbus.gen.Index;

public interface GameScoreEvent extends GameEvent {
    @Index(1) long getMillisecondTime();

    default double timeInSeconds() {
        return getMillisecondTime() / 1000d;
    }
}
