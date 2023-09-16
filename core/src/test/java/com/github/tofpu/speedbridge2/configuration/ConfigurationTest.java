package com.github.tofpu.speedbridge2.configuration;

import com.github.tofpu.speedbridge2.configuration.service.PluginConfigTypes;
import com.github.tofpu.speedbridge2.configuration.service.ConfigurationService;
import java.io.File;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// todo: figure out a way to write & fetch nested keys and values
public class ConfigurationTest {

    private final File runtimeDirectory = new File("test-resources/runtime");
    private final File defaultDirectory = new File("test-resources/default/configuration");
    private final ConfigurationService service = new ConfigurationService(runtimeDirectory);

    @BeforeEach
    void setUp() {
        cleanRuntime();
        service.load();
    }

    @SuppressWarnings("all")
    private void cleanRuntime() {
        File[] files = runtimeDirectory.listFiles();
        if (files != null && files.length != 0) {
            Arrays.stream(files).forEach(File::delete);
        }
        runtimeDirectory.delete();
    }

    @AfterEach
    void tearDown() {
        service.unload();
        cleanRuntime();
    }

    @Test
    void basic_test() {
        Configuration config = service.on(PluginConfigTypes.CONFIG);

        config.setString("daddy", "tofpu");
        config.setString("mommy", "also tofpu");
        config.setString("friendly.friend", "and also tofpu");

        Assertions.assertEquals("tofpu", config.getString("daddy", null));
        Assertions.assertEquals("also tofpu", config.getString("mommy", null));
        Assertions.assertNull(config.getString("unknown", null));
        Assertions.assertEquals("world", config.getString("hello", "world"));
        Assertions.assertEquals("and also tofpu", config.getString("friendly.friend", null));
    }

    @Test
    void nesting_test() {
        Configuration config = service.on(PluginConfigTypes.CONFIG);

        Assertions.assertEquals(123, config.path("hello")
            .setString("friend", "hi!")
            .setInt("id", 123)
            .getInt("id", -1));

        Assertions.assertEquals("hi!", config.path("hello").getString("friend", null));
        Assertions.assertEquals(123, config.path("hello").getInt("id", -1));
        Assertions.assertNull(config.path("hello").getString("unknown", null));
    }

    @Test
    void double_nesting_test() {
        Configuration config = service.on(PluginConfigTypes.CONFIG);

        final Map<String, Object> newYorkMap = new HashMap<>();
        newYorkMap.put("id", 1);
        newYorkMap.put("employees_size", 10);
        newYorkMap.put("director", "Tofpu");

        final Map<String, Object> newMexicoMap = new HashMap<>();
        newMexicoMap.put("id", 2);
        newMexicoMap.put("employees_size", 20);
        newMexicoMap.put("director", "AnotherTofpu");

        Date creationDate = Date.from(Instant.now());
        Date latestUpdate = Date.from(Instant.now().minus(200, TimeUnit.DAYS.toChronoUnit()));

        config.path("department")
            .setString("creationDate", creationDate.toString())
            .setString("latestUpdate", latestUpdate.toString())
            .path("list")
            .set("New York", newYorkMap)
            .set("New Mexico", newMexicoMap)
            .previous()
            .set("count", 20);

        Assertions.assertEquals(4, config.getAs(Map.class, "department", null).size());
        Assertions.assertEquals(2, config.path("department").getAs(Map.class, "list", null).size());

        Assertions.assertEquals(creationDate.toString(),
            config.path("department").getString("creationDate", null));
        Assertions.assertEquals(latestUpdate.toString(),
            config.path("department").getString("latestUpdate", null));
        Assertions.assertEquals(Map.of("New York", newYorkMap, "New Mexico", newMexicoMap),
            config.path("department").getAs(Map.class, "list", null));

        Assertions.assertEquals(newYorkMap,
            config.path("department").path("list").getAs(Map.class, "New York", null));
        Assertions.assertEquals(newMexicoMap,
            config.path("department").path("list").getAs(Map.class, "New Mexico", null));

        Assertions.assertEquals(20, config.path("department").getInt("count", -1));
    }

    @Test
    void test_config_types() {
        int i = 0;
        for (PluginConfigTypes value : PluginConfigTypes.values()) {
            LoadableConfiguration config = (LoadableConfiguration) service.on(value);
            config.setInt("index", i);

            Assertions.assertEquals(i, config.getInt("index", -1));
            Assertions.assertEquals("none", config.getString("hat", "none"));
            Assertions.assertNull(config.getAs(String.class, "unknown", null));
            i++;
        }
    }

    @Test
    void load_test() {
        LoadableConfiguration config = (LoadableConfiguration) service.on(PluginConfigTypes.CONFIG);
        config.load(new File(defaultDirectory, "config.yml"));

        Assertions.assertEquals("yes", config.getString("test", null));
//        Assertions.assertEquals("two", config.getString("default.one", null));
//        Assertions.assertEquals("four", config.getString("default.three", null));
        Assertions.assertNull(config.getString("unknown", null));
    }

    @Test
    void failure_test() {
        Configuration config = service.on(PluginConfigTypes.CONFIG);
        config.setString("test", "hello world!");
        Assertions.assertThrows(IllegalStateException.class, () -> config.getInt("test", -1));
    }
}
