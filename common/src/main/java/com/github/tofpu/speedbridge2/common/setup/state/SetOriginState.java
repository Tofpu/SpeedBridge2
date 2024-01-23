package com.github.tofpu.speedbridge2.common.setup.state;

import com.github.tofpu.speedbridge2.common.setup.IslandSetupData;
import com.github.tofpu.speedbridge2.common.setup.IslandSetupStates;
import com.github.tofpu.speedbridge2.object.Location;
import io.github.tofpu.speedbridge.gameengine.Game;
import io.github.tofpu.speedbridge.gameengine.GameStateType;
import io.github.tofpu.speedbridge.gameengine.StateChangeListener;

public class SetOriginState implements StateChangeListener<IslandSetupData> {
    @Override
    public void onGameStateChange(Game<IslandSetupData> game, GameStateType<IslandSetupData> stateChange) {
        IslandSetupData data = game.data();
        Location originalLocation = data.originalLocation();
        Location subtracted = data.land().getIslandLocation().subtract(originalLocation)
                .setYaw(originalLocation.getYaw()).setPitch(originalLocation.getPitch());

        data.origin(subtracted);
        System.out.println("Setting island's setup location to " + subtracted);
        game.dispatch(IslandSetupStates.STOP);
    }
}
