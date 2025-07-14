package io.tofpu.speedbridge2.score.domain.event;

public interface ScoreUnregisteredEvent extends ScoreRegistrationEvent {
    @Override
    default Type type() {
        return Type.UNREGISTERED;
    }
}
