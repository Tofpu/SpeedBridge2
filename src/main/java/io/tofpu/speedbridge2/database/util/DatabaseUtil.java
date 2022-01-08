package io.tofpu.speedbridge2.database.util;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class DatabaseUtil {
    public static <T> CompletableFuture<T> runAsync(final Supplier<?> supplier) {
        return (CompletableFuture<T>) CompletableFuture.supplyAsync(supplier);
    }

    public static CompletableFuture<Void> runAsync(final Runnable runnable) {
        return CompletableFuture.runAsync(runnable);
    }
}
