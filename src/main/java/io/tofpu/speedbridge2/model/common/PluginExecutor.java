package io.tofpu.speedbridge2.model.common;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;
import java.util.function.Supplier;

public final class PluginExecutor implements Executor {
    public static final @NotNull PluginExecutor INSTANCE = new PluginExecutor();

    public static @NotNull CompletableFuture<Void> runAsync(final Runnable runnable) {
        return (CompletableFuture<Void>) INSTANCE.submit(runnable);
    }

    public static <T> @NotNull CompletableFuture<T> supply(final Supplier<?> supplier) {
        return (CompletableFuture<T>) INSTANCE.supplyAsync(supplier);
    }

    private final @NotNull ExecutorService executor;

    public PluginExecutor() {
        this.executor = Executors.newFixedThreadPool(4);
    }

    @Override
    public void execute(final @NotNull Runnable command) {
        executor.execute(command);
    }

    public CompletableFuture<?> submit(final @NotNull Runnable runnable) {
        return CompletableFuture.runAsync(runnable, this);
    }

    public CompletableFuture<?> supplyAsync(final @NotNull Supplier<?> supplier) {
        return CompletableFuture.supplyAsync(supplier, this);
    }

    public void shutdown() {
        executor.shutdown();
    }
}
