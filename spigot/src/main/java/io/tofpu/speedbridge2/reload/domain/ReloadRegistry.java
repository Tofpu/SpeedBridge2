package io.tofpu.speedbridge2.reload.domain;

import java.util.Collection;
import java.util.Optional;

public interface ReloadRegistry extends Iterable<Reloadable> {
    void register(Reloadable reloadable);
    Optional<Reloadable> get(Reloadable.Key id);
    Collection<Reloadable> all();
}
