package io.tofpu.speedbridge2.domain.player.misc.stat;

import java.util.UUID;

public interface PlayerStat {
    UUID getOwner();

    String getKey();
    String getValue();

    void increment();
}
