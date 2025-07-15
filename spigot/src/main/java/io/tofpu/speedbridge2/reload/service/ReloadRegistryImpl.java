package io.tofpu.speedbridge2.reload.service;

import io.tofpu.speedbridge2.reload.domain.ReloadRegistry;
import io.tofpu.speedbridge2.reload.domain.Reloadable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class ReloadRegistryImpl implements ReloadRegistry {
    private final Map<Reloadable.Key, Reloadable> reloadables = new HashMap<>();

    @Override
    public void register(Reloadable reloadable) {
        Reloadable.Key key = reloadable.key();
        if (reloadables.containsKey(key)) {
            throw new IllegalArgumentException("Reloadable with key " + key.name() + " is already registered.");
        }
        reloadables.put(key, reloadable);
    }

    @Override
    public Optional<Reloadable> get(Reloadable.Key id) {
        return Optional.ofNullable(reloadables.get(id));
    }

    @Override
    public Collection<Reloadable> all() {
        return Collections.unmodifiableCollection(reloadables.values());
    }

    @Override
    public @NotNull Iterator<Reloadable> iterator() {
        return reloadables.values().iterator();
    }
}
