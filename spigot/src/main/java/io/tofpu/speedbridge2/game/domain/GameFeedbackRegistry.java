package io.tofpu.speedbridge2.game.domain;

import java.util.HashMap;
import java.util.Map;

public class GameFeedbackRegistry {
    private final Map<Type, GameFeedback> feedbackMap = new HashMap<>();

    public void register(Type type, GameFeedback feedback) {
        feedbackMap.put(type, feedback);
    }

    public GameFeedback get(Type type) {
        GameFeedback feedback = feedbackMap.get(type);
        if (feedback == null) {
            throw  new IllegalStateException("No feedback for type " + type);
        }
        return feedback;
    }

    public enum Type {
        BEATEN_SCORE,
        SCORE,
        RESET,
    }
}
