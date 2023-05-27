package com.github.tofpu.speedbridge2.object.player;

import com.github.tofpu.speedbridge2.object.Position;
import com.github.tofpu.speedbridge2.object.World;
import java.util.UUID;

public interface OnlinePlayer {

    UUID id();

    String name();

    void sendMessage(String content);

    void sendMessage(ConfigurableMessage message);

    void teleport(Position position);

    boolean hasPermission(String node);

    boolean isOperator();

    World getWorld();

    Position getPosition();
}
