package com.github.tofpu.speedbridge2.event;

import com.github.tofpu.speedbridge2.event.Listener;
import com.github.tofpu.speedbridge2.event.dispatcher.EventListener;
import com.github.tofpu.speedbridge2.event.event.PlayerLeaveEvent;

public class MessageListener implements Listener {

    @EventListener
    public void on(final MessageEvent event) {
    }

    @EventListener
    void on(PlayerLeaveEvent event) {
    }
}
