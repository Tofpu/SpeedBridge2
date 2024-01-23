package com.github.tofpu.speedbridge2.common.game;

import io.github.tofpu.speedbridge.gameengine.Game;
import io.github.tofpu.speedbridge.gameengine.GameData;
import io.github.tofpu.speedbridge.gameengine.GameStateType;
import io.github.tofpu.speedbridge.gameengine.StateManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StateManagerTest {
    private final StateManager<CountData> stateManager = new StateManager<>();

    @Test
    void name() {
        stateManager.addState(StateTypes.ODD, (game, prevState, stateChange) -> game.data().count += 1);
        stateManager.addState(StateTypes.EVEN, (game, prevState, stateChange) -> game.data().count += 1);

        CountData countData = new CountData(1);
        Game<CountData> game = new Game<>(countData, stateManager);

        assertThrows(Exception.class, () -> game.dispatch(StateTypes.EVEN));

        game.dispatch(StateTypes.ODD);
        assertEquals(2, countData.count);
    }

    enum StateTypes implements GameStateType<CountData> {
        ODD {
            @Override
            public boolean test(Game<CountData> game) {
                return game.data().count % 2 == 1;
            }
        },
        EVEN {
            @Override
            public boolean test(Game<CountData> game) {
                return game.data().count % 2 == 0;
            }
        }
    }

    class CountData extends GameData {
        private int count;

        CountData(int count) {
            this.count = count;
        }
    }
}
