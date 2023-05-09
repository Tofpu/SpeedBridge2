package io.tofpu.speedbridge2.game.service;

import java.util.HashMap;
import java.util.Map;

public class GameRegistry<K, V> {
    private final Map<K, V> gameMap = new HashMap<>();

    public void store(final K key, final V obj) {
        this.gameMap.put(key, obj);
    }

    public V getBy(final K key) {
        return this.gameMap.get(key);
    }

    public void remove(final K key) {
        this.gameMap.remove(key);
    }
}
