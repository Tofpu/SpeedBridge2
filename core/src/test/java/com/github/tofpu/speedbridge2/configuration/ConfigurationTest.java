package com.github.tofpu.speedbridge2.configuration;

import com.github.tofpu.speedbridge2.configuration.impl.AdvancedConfiguration;
import java.io.File;
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
        service.load();
    }

    @AfterEach
    void tearDown() {
        service.unload();
    }

    @Test
    void basic_test() {
        AdvancedConfiguration config = service.on(ConfigType.CONFIG);

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
    void test_config_types() {
        int i = 0;
        for (ConfigType value : ConfigType.values()) {
            AdvancedConfiguration config = service.on(value);
            config.setInt("index", i);

            Assertions.assertEquals(i, config.getInt("index", -1));
            Assertions.assertEquals("none", config.getString("hat", "none"));
            Assertions.assertNull(config.get("unknown", null));
            i++;
        }
    }

    @Test
    void load_test() {
        AdvancedConfiguration config = service.on(ConfigType.CONFIG);
        config.load(new File(defaultDirectory, "config.yml"));

        Assertions.assertEquals("yes", config.getString("test", null));
//        Assertions.assertEquals("two", config.getString("default.one", null));
//        Assertions.assertEquals("four", config.getString("default.three", null));
        Assertions.assertNull(config.getString("unknown", null));
    }

    @Test
    void failure_test() {
        AdvancedConfiguration config = service.on(ConfigType.CONFIG);
        config.setString("test", "hello world!");
        Assertions.assertThrows(IllegalStateException.class, () -> config.getInt("test", -1));
    }
}
