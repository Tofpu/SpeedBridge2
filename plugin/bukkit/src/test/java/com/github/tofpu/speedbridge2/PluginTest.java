package com.github.tofpu.speedbridge2;

import static com.github.tofpu.speedbridge2.util.SpigotTestUtil.clearBukkitServerInstance;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.github.tofpu.speedbridge2.plugin.BukkitPlugin;
import java.io.File;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PluginTest {
    private final Server server = mockServerWithLogger();

    @BeforeEach
    void setUp() {
        Bukkit.setServer(server);
    }

    @AfterEach
    void tearDown() {
        clearBukkitServerInstance();
    }

    @SuppressWarnings("deprecation")
    @Test
    void launch_test() {
        JavaPluginLoader javaPluginLoader = new JavaPluginLoader(server);
        PluginDescriptionFile descriptionFile = new PluginDescriptionFile("speedbridge2", "1.0.0", BukkitPlugin.class.getPackageName() + ".BukkitPlugin");

        File dataFolder = new File("test-resources/runtime/server/plugins/speedbridge2");
        File pluginFile = new File("test-resources/runtime/server/dummy.jar");

        BukkitPlugin bukkitPlugin = new BukkitPlugin(javaPluginLoader, server, descriptionFile, dataFolder, pluginFile);
        bukkitPlugin.onLoad();
        bukkitPlugin.onEnable();
        bukkitPlugin.onDisable();

        Assertions.assertTrue(BukkitPlugin.unitTesting());
    }

    private Server mockServerWithLogger() {
        Server mock = mock(Server.class);
        doReturn(Logger.getAnonymousLogger()).when(mock).getLogger();
        return mock;
    }
}
