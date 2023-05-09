package com.github.tofpu.speedbridge2.service;

public class DemoLoadableService implements LoadableService {
    private boolean loaded = false;

    @Override
    public void load() {
        loaded = true;
    }

    @Override
    public void unload() {
        loaded = false;
    }

    public boolean isLoaded() {
        return loaded;
    }
}
