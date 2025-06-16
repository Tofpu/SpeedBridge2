package io.tofpu.speedbridge2.game.event;

import io.github.revxrsal.eventbus.gen.Index;
import io.tofpu.speedbridge2.game.domain.Game;

public interface GameEvent {
    @Index(0)
    Game getGame();
}
