package io.tofpu.speedbridge2.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * The default implementation of {@link AsyncThreadExecutor} interface.
 */
public class DefaultAsyncThreadExecutor implements AsyncThreadExecutor {
    private final ScheduledExecutorService executor;

    public DefaultAsyncThreadExecutor(final int corePoolSize) {
        this.executor = Executors.newScheduledThreadPool(corePoolSize);
    }

    @Override
    public void execute(final Runnable runnable) {
        executor.execute(runnable);
    }

    @Override
    public CompletableFuture<Void> runAsync(final Runnable runnable) {
        return CompletableFuture.runAsync(runnable, executor);
    }

    @Override
    public <U> CompletableFuture<U> supplyAsync(final Supplier<U> supplier) {
        return CompletableFuture.supplyAsync(supplier, executor);
    }

    @Override
    public ScheduledFuture<?> schedule(
            final Runnable runnable, final long initialDelay, final long delay, final TimeUnit timeUnit) {
        return executor.scheduleWithFixedDelay(runnable, initialDelay, delay, timeUnit);
    }
}
