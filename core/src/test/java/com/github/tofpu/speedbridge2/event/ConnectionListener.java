package com.github.tofpu.speedbridge2.event;

import com.github.tofpu.speedbridge2.listener.Listener;
import com.github.tofpu.speedbridge2.listener.dispatcher.EventListener;
import com.github.tofpu.speedbridge2.listener.event.PlayerJoinEvent;
import com.github.tofpu.speedbridge2.listener.event.PlayerLeaveEvent;

public class ConnectionListener implements Listener {

    @EventListener
    public void on(PlayerJoinEvent event) {
    }

    @EventListener
    public void on(PlayerLeaveEvent event) {
    }
}
