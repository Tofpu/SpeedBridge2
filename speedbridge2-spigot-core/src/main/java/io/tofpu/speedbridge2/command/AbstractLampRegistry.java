package io.tofpu.speedbridge2.command;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractLampRegistry<K, V> {
    private final Map<K, V> parserMap = new HashMap<>();

    public void register(final K key, final V value) {
        this.parserMap.put(key, value);
    }

    public Collection<V> values() {
        return Collections.unmodifiableCollection(parserMap.values());
    }
}
