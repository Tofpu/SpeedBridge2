package io.tofpu.speedbridge2.database.user.repository.score.key;

import java.util.UUID;

public class ScoreUUID {
    public static ScoreUUID of(final UUID uuid, final int islandSlot) {
        return new ScoreUUID(uuid, islandSlot);
    }

    private final UUID uuid;
    private final int islandSlot;

    private ScoreUUID(final UUID uuid, final int islandSlot) {
        this.uuid = uuid;
        this.islandSlot = islandSlot;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getIslandSlot() {
        return islandSlot;
    }
}
