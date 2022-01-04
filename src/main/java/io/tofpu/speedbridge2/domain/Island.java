package io.tofpu.speedbridge2.domain;

import io.ebean.Model;
import org.bukkit.entity.Player;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "islands")
public final class Island extends Model {

    @Id
    private final int slot;

    @Transient
    private final Map<GamePlayer, GameIsland> islandMap = new HashMap<>();

    public Island(final int slot) {
        this.slot = slot;
    }

    public Map.Entry<GamePlayer, GameIsland> generateGame(final Player player) {
        final GamePlayer gamePlayer = GamePlayer.of(player);
        final GameIsland gameIsland = new GameIsland(this.slot, gamePlayer);

        this.islandMap.put(gamePlayer, gameIsland);
        return new AbstractMap.SimpleImmutableEntry<>(gamePlayer, gameIsland);
    }

    public GameIsland findGameByPlayer(final GamePlayer gamePlayer) {
        return this.islandMap.get(gamePlayer);
    }

    public int getSlot() {
        return slot;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Island{");
        sb.append("slot=").append(slot);
        sb.append(", islandMap=").append(islandMap);
        sb.append('}');
        return sb.toString();
    }
}
