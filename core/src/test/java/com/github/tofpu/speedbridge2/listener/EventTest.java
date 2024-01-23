package com.github.tofpu.speedbridge2.listener;

import com.github.tofpu.speedbridge2.event.MessageEvent;
import com.github.tofpu.speedbridge2.event.MessageListener;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.event.dispatcher.invoker.MethodInvoker;
import org.junit.jupiter.api.Assertions;
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
        Assertions.assertTrue(eventDispatcherService.isRegisteredListener(MessageListener.class));
        Assertions.assertTrue(eventDispatcherService.isRegisteredEvent(MessageEvent.class));

        MessageEvent messageEvent = new MessageEvent(sender, message);
        eventDispatcherService.unsafeDispatch(messageEvent);

        verify(messageListener, times(1)).on(messageEvent);
        verifyNoMoreInteractions(messageListener);
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
