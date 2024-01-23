package com.github.tofpu.speedbridge2.common.game.score;

import lombok.Data;

import java.util.UUID;

@Data
class PlayerIdSlot {
    private final UUID playerId;
    private final int islandSlot;

    public PlayerIdSlot(UUID playerId, int islandSlot) {
        this.playerId = playerId;
        this.islandSlot = islandSlot;
    }
}
