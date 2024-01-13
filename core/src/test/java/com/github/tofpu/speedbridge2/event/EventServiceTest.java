package com.github.tofpu.speedbridge2.event;

import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.event.dispatcher.EventListener;
import com.github.tofpu.speedbridge2.event.dispatcher.ListeningState;
import com.github.tofpu.speedbridge2.event.event.PlayerJoinEvent;
import com.github.tofpu.speedbridge2.event.event.PlayerLeaveEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

        dispatcherService.unsafeDispatch(new PlayerJoinEvent(mock()));
        verify(connectionListener, times(1)).on(any(PlayerJoinEvent.class));
        verifyNoMoreInteractions(connectionListener);
        verifyNoInteractions(messageListener);

        dispatcherService.unsafeDispatch(new PlayerLeaveEvent(mock()));
        verify(connectionListener, times(1)).on(any(PlayerLeaveEvent.class));
        verifyNoMoreInteractions(connectionListener);

        verify(messageListener, times(1)).on(any(PlayerLeaveEvent.class));
        verifyNoMoreInteractions(messageListener);

        dispatcherService.unsafeDispatch(new MessageEvent(UUID.randomUUID(), "Test"));
        verify(messageListener, times(1)).on(any(MessageEvent.class));
        verifyNoMoreInteractions(messageListener);
        verifyNoMoreInteractions(connectionListener);

        dispatcherService.unregister(connectionListener.getClass());
        Assertions.assertFalse(
                dispatcherService.isRegisteredListener(connectionListener.getClass()));

        dispatcherService.unsafeDispatch(new PlayerLeaveEvent(mock()));
        verify(messageListener, times(1)).on(any(PlayerLeaveEvent.class));
        verifyNoMoreInteractions(messageListener);
        verifyNoMoreInteractions(connectionListener);
    }

    @Test
    void monitor_state_test() {
        Queue<String> logs = new LinkedList<>();

        Listener listeningListener = new Listener() {
            @EventListener
            void on(PlayerJoinEvent ignoredEvent) {
                logs.add("listening");
            }
        };

        Listener monitoringListener = new Listener() {
            @EventListener(state = ListeningState.MONITORING)
            void on(PlayerJoinEvent ignoredEvent) {
                logs.add("monitoring");
            }
        };

        dispatcherService.register(monitoringListener);
        dispatcherService.register(listeningListener);
        dispatcherService.dispatchIfApplicable(new PlayerJoinEvent(mock()));

        assertEquals(2, logs.size());
        assertEquals("listening", logs.poll());
        assertEquals("monitoring", logs.poll());
    }

    @Test
    void pass_in_cancelled_event_with_a_listener_that_excludes_cancelled_events() {
        Queue<String> logs = new LinkedList<>();

        Listener listener = new Listener() {
            @EventListener(ignoreCancelled = true)
            void on(AnEventThatIsCancellable ignoredEvent) {
                logs.add("triggered");
            }
        };

        dispatcherService.register(listener);
        dispatcherService.dispatchIfApplicable(new AnEventThatIsCancellable(true));

        assertEquals(0, logs.size());
    }

    @Test
    void pass_in_cancellable_event_that_gets_cancelled_throughout_the_pipline_with_a_listener_that_excludes_cancelled_events() {
        Queue<String> logs = new LinkedList<>();

        Listener listenerThatCancelsEvents = new Listener() {
            @EventListener
            void on(AnEventThatIsCancellable ignoredEvent) {
                logs.add("cancelling event");
                ignoredEvent.cancel(true);
            }
        };

        Listener ignoreCancelledEventListener = new Listener() {
            @EventListener(ignoreCancelled = true)
            void on(AnEventThatIsCancellable ignoredEvent) {
                logs.add("triggered");
            }
        };

        dispatcherService.register(listenerThatCancelsEvents);
        dispatcherService.register(ignoreCancelledEventListener);
        dispatcherService.dispatchIfApplicable(new AnEventThatIsCancellable());

        assertEquals(1, logs.size());
    }

    static class AnEventThatIsCancellable extends Event implements Cancellable {
        private boolean cancelled = false;

        public AnEventThatIsCancellable(boolean cancelled) {
            this.cancelled = cancelled;
        }

        public AnEventThatIsCancellable() {
        }

        @Override
        public void cancel(boolean state) {
            cancelled = state;
        }

        @Override
        public boolean cancelled() {
            return cancelled;
        }
    }
}
