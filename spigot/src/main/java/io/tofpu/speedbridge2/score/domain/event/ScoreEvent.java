package io.tofpu.speedbridge2.score.domain.event;

import io.github.revxrsal.eventbus.gen.Index;
import io.tofpu.speedbridge2.score.domain.Score;

public interface ScoreEvent {
    @Index(0)
    Score score();
}
