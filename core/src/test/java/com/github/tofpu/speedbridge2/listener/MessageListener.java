package com.github.tofpu.speedbridge2.listener;

import com.github.tofpu.speedbridge2.event.Listener;
import com.github.tofpu.speedbridge2.event.dispatcher.EventListener;

public class MessageListener implements Listener {

    @EventListener
    protected void on(MessageEvent event) {
        System.out.printf("Received message %s by %s%n", event.getMessage(), event.getUserId());
    }

    protected void brokenOn(MessageEvent event) {
    }
}
