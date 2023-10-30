package com.github.tofpu.speedbridge2.bridge.setup;

import com.github.tofpu.speedbridge2.ArenaAdapter;
import com.github.tofpu.speedbridge2.bridge.IslandSchematic;
import com.github.tofpu.speedbridge2.bridge.Land;
import com.github.tofpu.speedbridge2.bridge.LandController;
import com.github.tofpu.speedbridge2.bridge.core.Game;
import com.github.tofpu.speedbridge2.bridge.core.GameHandler;
import com.github.tofpu.speedbridge2.bridge.core.state.StartGameState;
import com.github.tofpu.speedbridge2.bridge.core.state.StopGameState;
import com.github.tofpu.speedbridge2.bridge.game.Island;
import com.github.tofpu.speedbridge2.island.IslandService;
import com.github.tofpu.speedbridge2.lobby.LobbyService;
import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.object.World;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import com.github.tofpu.speedbridge2.schematic.Schematic;
import com.github.tofpu.speedbridge2.schematic.SchematicHandler;

import java.util.UUID;

@SuppressWarnings({"unchecked", "rawtypes"})
public class BridgeSetupHandler extends GameHandler<OnlinePlayer, BridgeSetupHandler, IslandSetup> {
    private final IslandService islandService;
    private final LobbyService lobbyService;
    private final ArenaAdapter arenaAdapter;
    private final LandController landController;
    private final SchematicHandler schematicHandler;

    public BridgeSetupHandler(IslandService islandService, LobbyService lobbyService, ArenaAdapter arenaAdapter, SchematicHandler schematicHandler) {
        this.islandService = islandService;
        this.lobbyService = lobbyService;
        this.arenaAdapter = arenaAdapter;
        this.landController = new LandController(new SetupArenaManager(arenaAdapter));
        this.schematicHandler = schematicHandler;
    }

    public void start(final OnlinePlayer player, final int slot, final String schematicName) {
        assertPlayerIsNotInGame(player);

        Schematic schematic = schematicHandler.resolve(schematicName);
        World world = arenaAdapter.gameWorld();
        Location origin = Location.zero(world);

        Island island = islandService.get(slot);
        if (island != null && island.getAbsolute() != null) {
            origin = island.getAbsolute();
        }

        IslandSchematic islandSchematic = new IslandSchematic(slot, schematic, origin);
        Land land = landController.reserveSpot(player.id(), islandSchematic, world);

        Game game = new IslandSetup(this, new SetupPlayer(player), slot, schematicName, land);
        super.internalStart(player, game);
    }

    public void setOrigin(UUID playerId, Location location) {
        getSafe(playerId).dispatch(new SetOriginState(this, location));
    }

    @Override
    protected Game.GameState createPrepareState() {
        return null;
    }

    @Override
    protected StartGameState createStartState() {
        return new BeginSetupState();
    }

    @Override
    protected StopGameState createStopState() {
        return new EndSetupState(islandService, lobbyService, landController);
    }
}
