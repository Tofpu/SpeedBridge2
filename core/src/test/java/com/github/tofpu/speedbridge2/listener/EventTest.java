package com.github.tofpu.speedbridge2.listener;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.tofpu.speedbridge2.listener.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.listener.dispatcher.invoker.MethodInvoker;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EventTest {

    private final EventDispatcherService eventDispatcherService = new EventDispatcherService();

    @Test
    void basic() {
        final UUID sender = UUID.randomUUID();
        final String message = "Hello World!";

        MessageListener messageListener = spy(MessageListener.class);
        eventDispatcherService.register(messageListener);
        Assertions.assertTrue(eventDispatcherService.isRegisteredListener(MessageListener.class));
        Assertions.assertTrue(eventDispatcherService.isRegisteredEvent(MessageEvent.class));

        MessageEvent messageEvent = new MessageEvent(sender, message);
        eventDispatcherService.dispatch(messageEvent);

        verify(messageListener, times(1)).on(messageEvent);
        verify(messageListener, never()).brokenOn(messageEvent);
    }

    @Test
    void invoke_test() throws NoSuchMethodException {
        MessageListener object = spy(new MessageListener());
        MethodInvoker invoker = new MethodInvoker(object,
            MessageListener.class.getDeclaredMethod("on", MessageEvent.class));
        invoker.invoke(new MessageEvent(UUID.randomUUID(), "test"));

        verify(object, times((1))).on(any());
    }
}
