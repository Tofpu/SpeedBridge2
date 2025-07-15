package io.tofpu.speedbridge2.reload.service;

import io.tofpu.speedbridge2.reload.domain.ReloadRegistry;
import io.tofpu.speedbridge2.reload.domain.Reloadable;

public class ReloadService {
    private final ReloadRegistry reloadRegistry;

    public ReloadService(ReloadRegistry reloadRegistry) {
        this.reloadRegistry = reloadRegistry;
    }

    public void reload(Reloadable.Key key) {
        reloadRegistry.get(key)
            .ifPresentOrElse(
                    ReloadService::handleReload,
                () -> System.err.println("No reloadable found for key: " + key.name())
            );
    }

    public void reloadAll() {
        for (Reloadable reloadable : reloadRegistry) {
            handleReload(reloadable);
        }
    }

    private static void handleReload(Reloadable reloadable) {
        try {
            reloadable.onReload();
        } catch (Exception e) {
            System.err.println("Failed to reload " + reloadable.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}
