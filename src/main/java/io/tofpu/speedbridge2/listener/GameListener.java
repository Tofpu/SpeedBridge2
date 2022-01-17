package io.tofpu.speedbridge2.listener;

import org.bukkit.event.Listener;

public class GameListener implements Listener {
    public GameListener() {
        ListenerManager.add(this);
    }
}
