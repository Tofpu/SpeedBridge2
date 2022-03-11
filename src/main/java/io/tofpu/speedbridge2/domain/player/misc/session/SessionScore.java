package io.tofpu.speedbridge2.domain.player.misc.session;

import io.tofpu.speedbridge2.domain.player.misc.score.Score;

import java.util.Collection;

public interface SessionScore {
    /**
     * Get all the scores for a session
     *
     * @return A collection of Score objects.
     */
    Collection<Score> getSessionScores();
    /**
     * Reset the session scores for all users
     */
    void resetSessionScores();
}
