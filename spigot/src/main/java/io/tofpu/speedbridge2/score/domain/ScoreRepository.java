package io.tofpu.speedbridge2.score.domain;

import java.util.Collection;

public interface ScoreRepository {
    void save(Score score);
    boolean delete(Score score);
    Collection<Score> findAll();
}
