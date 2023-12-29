package com.github.tofpu.speedbridge2.bridge.setup;

import com.github.tofpu.speedbridge2.ArenaAdapter;
import com.github.tofpu.speedbridge2.bridge.setup.state.SetupStateProvider;
import com.github.tofpu.speedbridge2.game.GameHandler;
import com.github.tofpu.speedbridge2.game.GameState;
import com.github.tofpu.speedbridge2.game.land.Land;
import com.github.tofpu.speedbridge2.game.land.LandController;
import com.github.tofpu.speedbridge2.game.land.arena.IslandSchematic;
import com.github.tofpu.speedbridge2.game.state.StartGameState;
import com.github.tofpu.speedbridge2.game.state.StopGameState;
import com.github.tofpu.speedbridge2.island.Island;
import com.github.tofpu.speedbridge2.island.IslandService;
import com.github.tofpu.speedbridge2.lobby.LobbyService;
import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.object.World;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import com.github.tofpu.speedbridge2.schematic.Schematic;
import com.github.tofpu.speedbridge2.schematic.SchematicHandler;

import java.util.UUID;

@SuppressWarnings({"unchecked", "rawtypes"})
public class BridgeSetupHandler extends GameHandler<OnlinePlayer, IslandSetupData> {
    private final IslandService islandService;
    private final ArenaAdapter arenaAdapter;
    private final LandController landController;
    private final SchematicHandler schematicHandler;
    private final SetupStateProvider stateProvider;

    public BridgeSetupHandler(IslandService islandService, LobbyService lobbyService, ArenaAdapter arenaAdapter, SchematicHandler schematicHandler) {
        this.islandService = islandService;
        this.arenaAdapter = arenaAdapter;
        this.landController = new LandController(new SetupArenaManager(arenaAdapter));
        this.schematicHandler = schematicHandler;
        this.stateProvider = new SetupStateProvider(islandService, lobbyService, landController);
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

        IslandSetup game = new IslandSetup(new IslandSetupData(new SetupPlayer(player), slot, schematicName, land));
        super.prepareAndRegister(player, game);
    }

    public void setOrigin(UUID playerId, Location location) {
        getSafe(playerId).dispatch(stateProvider.originState(location));
    }

    @Override
    protected GameState<IslandSetupData> createPrepareState() {
        return null;
    }

    @Override
    protected StartGameState createStartState() {
        return stateProvider.startState();
    }

    @Override
    protected StopGameState createStopState() {
        return stateProvider.stopState();
    }
}
