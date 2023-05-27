package com.github.tofpu.speedbridge2.event;

import com.github.tofpu.speedbridge2.event.Listener;
import com.github.tofpu.speedbridge2.event.dispatcher.EventListener;
import com.github.tofpu.speedbridge2.event.event.PlayerJoinEvent;
import com.github.tofpu.speedbridge2.event.event.PlayerLeaveEvent;

public class ConnectionListener implements Listener {

    @EventListener
    public void on(PlayerJoinEvent event) {
    }

    @EventListener
    public void on(PlayerLeaveEvent event) {
    }
}
