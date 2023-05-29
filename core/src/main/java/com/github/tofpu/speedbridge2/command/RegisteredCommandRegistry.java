package com.github.tofpu.speedbridge2.command;

import java.util.HashMap;
import java.util.Map;

public class RegisteredCommandRegistry<T extends CommandDetail> {
    private final Map<String, T> commandMap = new HashMap<>();

    public void register(String commandName, T object) {
        this.commandMap.put(commandName, object);
    }

    public T get(String commandName) {
        return this.commandMap.get(commandName);
    }

    public boolean contains(String commandName) {
        return this.commandMap.containsKey(commandName);
    }
}
