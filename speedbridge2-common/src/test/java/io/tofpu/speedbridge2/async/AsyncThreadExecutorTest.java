package io.tofpu.speedbridge2.async;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AsyncThreadExecutorTest {
    @Test
    public void test_run_async() throws ExecutionException, InterruptedException {
        final AsyncThreadExecutor asyncThreadExecutor = new DefaultAsyncThreadExecutor(1);
        final AtomicReference<Thread> asyncThreadReference = new AtomicReference<>();

        final Thread mainThread = Thread.currentThread();

        asyncThreadExecutor.runAsync(() -> asyncThreadReference.set(Thread.currentThread()))
                .get();

        assertNotEquals(mainThread, asyncThreadReference.get());
    }

    @Test
    public void test_supply_async() throws ExecutionException, InterruptedException {
        final AsyncThreadExecutor asyncThreadExecutor = new DefaultAsyncThreadExecutor(1);

        final Thread mainThread = Thread.currentThread();
        final Thread asyncThread = asyncThreadExecutor.supplyAsync(Thread::currentThread)
                .get();

        assertNotEquals(mainThread, asyncThread);
    }

    @Test
    @Timeout(2)
    public void test_execute() throws InterruptedException {
        final AsyncThreadExecutor asyncThreadExecutor = new DefaultAsyncThreadExecutor(1);
        final AtomicReference<Thread> asyncThreadReference = new AtomicReference<>();

        final Thread mainThread = Thread.currentThread();

        asyncThreadExecutor.execute(() -> asyncThreadReference.set(Thread.currentThread()));

        // in case async was having a bad day
        while (asyncThreadReference.get() == null) {
            Thread.sleep(100);
        }

        assertNotEquals(mainThread, asyncThreadReference.get());
    }

    @Test
    @Timeout(5)
    public void test_schedule() throws InterruptedException {
        final AsyncThreadExecutor asyncThreadExecutor = new DefaultAsyncThreadExecutor(1);
        final AtomicReference<Thread> asyncThreadReference = new AtomicReference<>();

        final Thread mainThread = Thread.currentThread();
        final AtomicInteger counter = new AtomicInteger();

        // our goal is to test whether the code
        // gets repeated in a concise manner
        final ScheduledFuture<?> schedule =
                asyncThreadExecutor.schedule(() -> {
                    if (counter.get() == 2) {
                        asyncThreadReference.set(Thread.currentThread());
                    }
                    counter.getAndIncrement();
                }, 1, 1, TimeUnit.SECONDS);

        // wait until counter reaches count 3
        while (counter.get() <= 3) {
            Thread.sleep(100);
        }
        schedule.cancel(true);

        // ensure that the async thread
        // wasn't having a bad day
        assertNotNull(asyncThreadReference.get());
        assertNotEquals(mainThread, asyncThreadReference.get());
    }
}
