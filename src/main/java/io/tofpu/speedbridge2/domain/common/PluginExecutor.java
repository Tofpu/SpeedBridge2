package io.tofpu.speedbridge2.domain.common;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public final class PluginExecutor implements Executor {
    public static final @NotNull PluginExecutor INSTANCE = new PluginExecutor();

    public static CompletableFuture<Void> runAsync(final Runnable runnable) {
        return (CompletableFuture<Void>) INSTANCE.submit(runnable);
    }

    public static CompletableFuture<?> supply(final Supplier<?> supplier) {
        return INSTANCE.supplyAsync(supplier);
    }

    private final @NotNull ExecutorService executor;

    public PluginExecutor() {
        this.executor = Executors.newCachedThreadPool();
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