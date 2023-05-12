package com.github.tofpu.speedbridge2.listener;

public class MessageListener extends Listener<MessageEvent> {
    @Override
    protected void on(MessageEvent event) {
        System.out.printf("Received message %s by %s%n", event.getMessage(), event.getUserId());
    }

    @Override
    public Class<MessageEvent> type() {
        return MessageEvent.class;
    }
}
