package io.tofpu.speedbridge2.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * An interface responsible for
 * handling asynchronous execution-related tasks.
 *
 * @see DefaultAsyncThreadExecutor
 */
public interface AsyncThreadExecutor {
    void execute(Runnable runnable);
    CompletableFuture<Void> runAsync(Runnable runnable);
    <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier);
    ScheduledFuture<?> schedule(
            Runnable runnable, long initialDelay, long delay, TimeUnit timeUnit);
}
