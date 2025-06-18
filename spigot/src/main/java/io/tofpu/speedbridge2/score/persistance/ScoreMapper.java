package io.tofpu.speedbridge2.score.persistance;

import io.tofpu.speedbridge2.score.domain.Score;

public class ScoreMapper {
    public ScoreEntity toEntity(Score score) {
        return new ScoreEntity(
                score.playerId(),
                score.slot(),
                score.time(),
                score.timestamp()
        );
    }

    public Score toScore(ScoreEntity entity) {
        return new Score(
                entity.playerId(),
                entity.slot(),
                entity.time(),
                entity.timestamp()
        );
    }
}
