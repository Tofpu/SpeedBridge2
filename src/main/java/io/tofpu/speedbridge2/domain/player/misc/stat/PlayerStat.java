package io.tofpu.speedbridge2.domain.player.misc.stat;

public interface PlayerStat {
    String getKey();
    String getValue();

    void increment();
}
