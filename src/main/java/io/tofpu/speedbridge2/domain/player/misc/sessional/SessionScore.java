package io.tofpu.speedbridge2.domain.player.misc.sessional;

import io.tofpu.speedbridge2.domain.player.misc.score.Score;

import java.util.Collection;

public interface SessionScore {
    Collection<Score> getSessionScores();
    void resetSessionScores();
}
