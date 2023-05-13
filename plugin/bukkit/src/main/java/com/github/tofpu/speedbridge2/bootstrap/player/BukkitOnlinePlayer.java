package com.github.tofpu.speedbridge2.bootstrap.player;

import com.github.tofpu.speedbridge2.adapter.BukkitAdapter;
import com.github.tofpu.speedbridge2.adapter.SpeedBridgeAdapter;
import com.github.tofpu.speedbridge2.object.generic.Position;
import com.github.tofpu.speedbridge2.object.generic.World;
import com.github.tofpu.speedbridge2.player.OnlinePlayer;
import java.util.UUID;
import org.bukkit.entity.Player;

public class BukkitOnlinePlayer implements OnlinePlayer {

    private final Player player;

    public BukkitOnlinePlayer(Player player) {
        this.player = player;
    }

    @Override
    public UUID id() {
        return player.getUniqueId();
    }

    @Override
    public String name() {
        return player.getName();
    }

    @Override
    public void sendMessage(String content) {
        player.sendMessage(content);
    }

    @Override
    public void teleport(Position position) {
        player.teleport(SpeedBridgeAdapter.toLocation(position));
    }

    @Override
    public boolean hasPermission(String node) {
        return player.hasPermission(node);
    }

    @Override
    public boolean isOperator() {
        return player.isOp();
    }

    @Override
    public World getWorld() {
        return BukkitAdapter.toWorld(player.getWorld());
    }

    @Override
    public Position getPosition() {
        return BukkitAdapter.toPosition(player.getLocation());
    }
}
