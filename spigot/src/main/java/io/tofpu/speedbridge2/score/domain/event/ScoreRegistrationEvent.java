package io.tofpu.speedbridge2.score.domain.event;

public interface ScoreRegistrationEvent extends ScoreEvent {
    Type type();

    enum Type {
        REGISTERED, UNREGISTERED
    }
}
