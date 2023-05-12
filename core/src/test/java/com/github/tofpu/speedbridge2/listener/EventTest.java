package com.github.tofpu.speedbridge2.listener;

import com.github.tofpu.speedbridge2.listener.dispatcher.EventDispatcherService;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;

public class EventTest {
    private final EventDispatcherService eventDispatcherService = new EventDispatcherService();

    @Test
    void basic() {
        final UUID sender = UUID.randomUUID();
        final String message = "Hello World!";

        MessageListener messageListener = spy(MessageListener.class);
        eventDispatcherService.register(messageListener);

        MessageEvent messageEvent = new MessageEvent(sender, message);
        eventDispatcherService.dispatch(messageEvent);

        verify(messageListener, times(1)).on(messageEvent);
    }
}