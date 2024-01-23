package com.github.tofpu.speedbridge2.object.player;

import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.object.Position;
import com.github.tofpu.speedbridge2.object.World;

import java.util.UUID;

public interface OnlinePlayer {

    UUID id();

    String name();

    void sendMessage(String content);

    void sendMessage(ConfigurableMessage message);
    void sendMessage(ConfigurableMessage message, Object... replace);

    void teleport(Position position);

    void teleport(Location location);

    boolean hasPermission(String node);

    boolean isOperator();

    World getWorld();

    Position getPosition();
}
