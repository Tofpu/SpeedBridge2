package io.tofpu.speedbridge2;

import io.github.revxrsal.eventbus.EventBus;
import io.github.revxrsal.eventbus.EventBusBuilder;
import io.tofpu.multiworldedit.MultiWorldEditAPI;
import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.database.system.DatabaseSystem;
import io.tofpu.speedbridge2.environment.infra.EnvironmentHandler;
import io.tofpu.speedbridge2.game.GameSystem;
import io.tofpu.speedbridge2.island.system.IslandSystem;
import io.tofpu.speedbridge2.lobby.system.LobbySystem;
import io.tofpu.speedbridge2.schematic.infra.SchematicHandler;
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
    private final EventBus eventBus = EventBusBuilder.asm()
            .build();

    private CommandHandler commandHandler;
    private DatabaseSystem databaseSystem;
    private GameSystem gameSystem;
    private ToolbarHandler toolbarHandler;
    private LobbySystem lobbySystem;
    private IslandSystem islandSystem;
    private SchematicHandler schematicHandler;

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

        lobbySystem = new LobbySystem();
        toolbarHandler = new ToolbarHandler(this);

        schematicHandler = new SchematicHandler(schematicDirectory());

        islandSystem = new IslandSystem();
        islandSystem.load(databaseSystem.database(), schematicHandler);
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
//        WorldEdit.getInstance().getConfiguration().saveDir
        File worldEditDirectory = new File(pluginsDirectory, "WorldEdit");
        return new File(worldEditDirectory, "schematics");
    }

    @Override
    public void onEnable() {
        commandHandler = new CommandHandler(this);
        islandSystem.registerCommands(commandHandler);

        lobbySystem.registerCommands(commandHandler);
        toolbarHandler.enable();

        EnvironmentHandler environmentHandler = new EnvironmentHandler(Bukkit.getWorldContainer());
        environmentHandler.setupEnvironment();

        ListenerRegistration listenerRegistration = ListenerRegistration.create(this);
        gameSystem = new GameSystem(
                environmentHandler, getDataFolder(), listenerRegistration,
                lobbySystem.lobbyService(), toolbarHandler.toolbarAPI()
        );
        gameSystem.enable();
        gameSystem.registerCommand(commandHandler);

        SetupSystem setupSystem = new SetupSystem(
                eventBus,
                islandSystem.islandService(),
                lobbySystem.lobbyService(),
                environmentHandler.getWorld()
        );

        setupSystem.registerListeners(toolbarHandler.toolbarAPI());
        setupSystem.registerCommand(commandHandler, schematicHandler);
        commandHandler.enable();
    }

    @Override
    public void onDisable() {
        if (toolbarHandler != null) {
            toolbarHandler.disable();
        }
    }
}
