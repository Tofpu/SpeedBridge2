package io.tofpu.speedbridge2;

import be.seeseemelk.mockbukkit.MockBukkit;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import io.tofpu.speedbridge2.model.island.IslandService;
import io.tofpu.speedbridge2.model.island.object.Island;
import io.tofpu.speedbridge2.plugin.SpeedBridgePlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IslandServiceTest {
    private static SpeedBridge plugin;

    @BeforeAll
    public static void load() {
        MockBukkit.mock().getPluginManager().loadPlugin(WorldEditPlugin.class, new Object[]{});
        final JavaPlugin mockedPlugin = MockBukkit.mock()
                .getPluginManager()
                .loadPlugin(SpeedBridgePlugin.class, new Object[]{});
        final SpeedBridge speedBridge = new SpeedBridge(mockedPlugin, true);
        try {
            plugin = MockBukkit.load(SpeedBridgePlugin.class, speedBridge).getSpeedBridge();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unload();
    }

    @Test
    public void testCreateIsland() {
        final IslandService islandService = new IslandService();
        try {
            final Island mockedIsland = mock(Island.class);
            when(mockedIsland.getSchematicClipboard()).
                    thenReturn(mock(Clipboard.class));

            // mock island or create new interface for it
            islandService.registerIsland(mockedIsland);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        assertNotNull(islandService.findIslandBy(1));
    }
}
