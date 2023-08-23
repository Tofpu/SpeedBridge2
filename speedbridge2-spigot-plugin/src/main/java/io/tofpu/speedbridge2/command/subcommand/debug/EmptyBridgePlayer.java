package io.tofpu.speedbridge2.command.subcommand.debug;

import io.tofpu.speedbridge2.model.player.object.BridgePlayer;

import java.util.UUID;

public class EmptyBridgePlayer extends BridgePlayer {
    protected EmptyBridgePlayer(UUID id) {
        super(null, null, id);
    }
}
