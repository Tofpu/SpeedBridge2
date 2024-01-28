package com.github.tofpu.speedbridge2.bukkit.command.subcommand;

import com.github.tofpu.speedbridge2.common.game.BridgeSystem;
import com.github.tofpu.speedbridge2.common.setup.GameSetupSystem;

public class CoreCommandExtension {
    public CoreCommand[] commands(BridgeSystem bridgeSystem, GameSetupSystem setupSystem) {
        return new CoreCommand[] {
                new GameCommand(bridgeSystem),
                new SetupCommand(setupSystem)
        };
    }
}
