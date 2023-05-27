package com.github.tofpu.speedbridge2.event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.github.tofpu.speedbridge2.listener.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.listener.event.PlayerJoinEvent;
import com.github.tofpu.speedbridge2.listener.event.PlayerLeaveEvent;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EventServiceTest {

    private final EventDispatcherService dispatcherService = new EventDispatcherService();

    @Test
    void basic_test() {
        ConnectionListener connectionListener = spy(new ConnectionListener());
        MessageListener messageListener = spy(new MessageListener());

        dispatcherService.register(connectionListener);
        dispatcherService.isRegisteredListener(connectionListener.getClass());

        dispatcherService.register(messageListener);
        dispatcherService.isRegisteredListener(messageListener.getClass());

        dispatcherService.dispatch(new PlayerJoinEvent(mock()));
        verify(connectionListener, times(1)).on(any(PlayerJoinEvent.class));
        verifyNoMoreInteractions(connectionListener);
        verifyNoInteractions(messageListener);

        dispatcherService.dispatch(new PlayerLeaveEvent(mock()));
        verify(connectionListener, times(1)).on(any(PlayerLeaveEvent.class));
        verifyNoMoreInteractions(connectionListener);

        verify(messageListener, times(1)).on(any(PlayerLeaveEvent.class));
        verifyNoMoreInteractions(messageListener);

        dispatcherService.dispatch(new MessageEvent(UUID.randomUUID(), "Test"));
        verify(messageListener, times(1)).on(any(MessageEvent.class));
        verifyNoMoreInteractions(messageListener);
        verifyNoMoreInteractions(connectionListener);

        dispatcherService.unregister(connectionListener.getClass());
        Assertions.assertFalse(
            dispatcherService.isRegisteredListener(connectionListener.getClass()));

        dispatcherService.dispatch(new PlayerLeaveEvent(mock()));
        verify(messageListener, times(1)).on(any(PlayerLeaveEvent.class));
        verifyNoMoreInteractions(messageListener);
        verifyNoMoreInteractions(connectionListener);
    }
}
