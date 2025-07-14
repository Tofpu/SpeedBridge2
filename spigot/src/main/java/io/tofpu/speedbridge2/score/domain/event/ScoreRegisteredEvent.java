package io.tofpu.speedbridge2.score.domain.event;

public interface ScoreRegisteredEvent extends ScoreRegistrationEvent {
    @Override
    default Type type() {
        return Type.REGISTERED;
    }
}
