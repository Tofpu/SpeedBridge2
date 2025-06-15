package io.tofpu.speedbridge2;

import io.tofpu.multiworldedit.MultiWorldEditAPI;
import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.database.system.DatabaseSystem;
import io.tofpu.speedbridge2.environment.EnvironmentService;
import io.tofpu.speedbridge2.game.GameSystem;
import io.tofpu.speedbridge2.island.system.IslandSystem;
import io.tofpu.speedbridge2.lobby.LobbyService;
import io.tofpu.speedbridge2.schematic.SchematicService;
import io.tofpu.speedbridge2.setup.system.SetupSystem;
import io.tofpu.speedbridge2.toolbar.ToolbarHandler;
import io.tofpu.speedbridge2.util.listener.ListenerRegistration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SpeedbridgePlugin extends JavaPlugin {
    private CommandHandler commandHandler;
    private DatabaseSystem databaseSystem;
    private GameSystem gameSystem;
    private ToolbarHandler toolbarHandler;
    private LobbyService lobbyService;
    private IslandSystem islandSystem;
    private SchematicService schematicService;

    @Override
    public void onLoad() {
        MultiWorldEditAPI.load(this);

        databaseSystem = new DatabaseSystem(
                new File(getDataFolder(), "database-settings.yml"),
                getDataFolder()
        );
        databaseSystem.load();
        databaseSystem.database().handle(connection -> {
            String schema = loadSchemaSQL(this);
            executeSchema(connection, schema);
        });

        lobbyService = new LobbyService();
        toolbarHandler = new ToolbarHandler(this);

        schematicService = new SchematicService(schematicDirectory());

        islandSystem = new IslandSystem();
        islandSystem.load(databaseSystem.database(), schematicService);
    }

    public String loadSchemaSQL(JavaPlugin javaPlugin) {
        try (InputStream in = javaPlugin.getResource("schema.sql")) {
            if (in == null) throw new FileNotFoundException("schema.sql not found in resources!");
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void executeSchema(Connection connection, String sql) {
        try (Statement statement = connection.createStatement()) {
            for (String part : sql.split(";")) {
                String trimmed = part.trim();
                if (!trimmed.isEmpty()) {
                    statement.execute(trimmed);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private @NotNull File schematicDirectory() {
        File pluginsDirectory = getDataFolder().getParentFile();
        File worldEditDirectory = new File(pluginsDirectory, "worldedit");
        return new File(worldEditDirectory, "schematics");
    }

    @Override
    public void onEnable() {
        commandHandler = new CommandHandler(this);
        islandSystem.registerCommands(commandHandler);

        toolbarHandler.enable();

        EnvironmentService environmentService = new EnvironmentService(Bukkit.getWorldContainer());
        environmentService.setupEnvironment();

        ListenerRegistration listenerRegistration = ListenerRegistration.create(this);
        gameSystem = new GameSystem(
                environmentService, getDataFolder(), listenerRegistration,
                lobbyService, toolbarHandler.toolbarAPI()
        );
        gameSystem.enable();
        gameSystem.registerCommand(commandHandler);

        SetupSystem setupSystem = new SetupSystem(
                islandSystem.islandService(),
                lobbyService,
                environmentService.getWorld()
        );
        setupSystem.registerCommand(commandHandler, schematicService);

        commandHandler.enable();
    }

    @Override
    public void onDisable() {
        if (toolbarHandler != null) {
            toolbarHandler.disable();
        }
    }
}
