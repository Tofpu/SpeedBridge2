package com.github.tofpu.speedbridge2.bukkit;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.github.tofpu.speedbridge2.bukkit.plugin.BukkitPlugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BukkitPluginTest {

    private ServerMock server;
    private BukkitPlugin plugin;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(BukkitPlugin.class);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unload();
    }

    @Test
    void server_plugin_null_check() {
        Assertions.assertNotNull(server);
        Assertions.assertNotNull(plugin);
    }

    @Test
    void name() {

    }
}
