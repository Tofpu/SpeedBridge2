package io.tofpu.speedbridge2.model.player.object.session;

import io.tofpu.speedbridge2.model.player.object.score.Score;

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
