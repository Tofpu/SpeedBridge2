package com.github.tofpu.speedbridge2.player;

import com.github.tofpu.speedbridge2.object.generic.Position;
import com.github.tofpu.speedbridge2.object.generic.World;

import java.util.UUID;

public interface OnlinePlayer {
    UUID id();
    String name();

    void sendMessage(String content);
    void teleport(Position position);

    World getWorld();
    Position getPosition();
}
