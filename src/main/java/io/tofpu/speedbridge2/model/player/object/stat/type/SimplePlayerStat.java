package io.tofpu.speedbridge2.model.player.object.stat.type;

import com.google.common.base.Objects;
import io.tofpu.speedbridge2.model.common.database.Databases;
import io.tofpu.speedbridge2.model.player.object.stat.PlayerStat;

import java.util.UUID;

public final class SimplePlayerStat implements PlayerStat {
    private final String key;
    private final UUID owner;

    private int value = 0;

    public SimplePlayerStat(final String key, final UUID owner) {
        this.key = key;
        this.owner = owner;
    }

    public SimplePlayerStat(final String key, final UUID owner, final int totalWins) {
        this(key, owner);
        this.value = totalWins;
    }

    @Override
    public UUID getOwner() {
        return this.owner;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public String getValue() {
        return this.value + "";
    }

    @Override
    public void increment() {
        value++;
        Databases.STATS_DATABASE.update(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SimplePlayerStat{");
        sb.append("key='")
                .append(key)
                .append('\'');
        sb.append(", owner=")
                .append(owner);
        sb.append(", value=")
                .append(value);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimplePlayerStat)) {
            return false;
        }
        final SimplePlayerStat that = (SimplePlayerStat) o;
        return Objects.equal(getKey(), that.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getKey());
    }
}
