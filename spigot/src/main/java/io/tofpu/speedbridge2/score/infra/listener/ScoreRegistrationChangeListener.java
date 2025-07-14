package io.tofpu.speedbridge2.score.infra.listener;

import io.github.revxrsal.eventbus.SubscribeEvent;
import io.tofpu.speedbridge2.score.domain.Score;
import io.tofpu.speedbridge2.score.domain.ScoreRepository;
import io.tofpu.speedbridge2.score.domain.event.ScoreRegistrationEvent;

public class ScoreRegistrationChangeListener {
    private final ScoreRepository scoreRepository;

    public ScoreRegistrationChangeListener(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    @SubscribeEvent
    public void onScoreRegistrationChanges(ScoreRegistrationEvent event) {
        ScoreRegistrationEvent.Type type = event.type();
        Score score = event.score();
        if (type == ScoreRegistrationEvent.Type.REGISTERED) {
            scoreRepository.save(score);
        } else if (type == ScoreRegistrationEvent.Type.UNREGISTERED) {
            scoreRepository.delete(score);
        }
    }
}
