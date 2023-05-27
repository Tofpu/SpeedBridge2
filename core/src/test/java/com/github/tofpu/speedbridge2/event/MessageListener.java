package com.github.tofpu.speedbridge2.event;

import com.github.tofpu.speedbridge2.listener.Listener;
import com.github.tofpu.speedbridge2.listener.dispatcher.EventListener;
import com.github.tofpu.speedbridge2.listener.event.PlayerLeaveEvent;

public class MessageListener implements Listener {
    @EventListener
    public void on(final MessageEvent event) {}

    @EventListener
    void on(PlayerLeaveEvent event) {}
}
