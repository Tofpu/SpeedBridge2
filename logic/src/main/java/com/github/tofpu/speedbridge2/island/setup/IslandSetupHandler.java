package com.github.tofpu.speedbridge2.island.setup;

import com.github.tofpu.speedbridge2.game.core.Game;
import com.github.tofpu.speedbridge2.game.core.GameHandler;
import com.github.tofpu.speedbridge2.game.core.arena.ArenaManager;
import com.github.tofpu.speedbridge2.game.core.state.StartGameState;
import com.github.tofpu.speedbridge2.game.core.state.StopGameState;
import com.github.tofpu.speedbridge2.game.island.Island;
import com.github.tofpu.speedbridge2.game.island.arena.Land;
import com.github.tofpu.speedbridge2.island.IslandService;
import com.github.tofpu.speedbridge2.lobby.LobbyService;
import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.object.World;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

public class IslandSetupHandler extends GameHandler<OnlinePlayer> {
    private final IslandService islandService;
    private final LobbyService lobbyService;
    private final ArenaManager arenaManager;
    private final World world;

    private final Map<UUID, Land> playerLandMapping = new HashMap<>();

    public IslandSetupHandler(IslandService islandService, LobbyService lobbyService, ArenaManager arenaManager, World world) {
        this.islandService = islandService;
        this.lobbyService = lobbyService;
        this.arenaManager = arenaManager;
        this.world = world;
    }

    public void start(final OnlinePlayer player, final int slot, final File schematicFile) {
        requireState(!isInGame(player.id()), "%s is already in a game!");

        // temporally until we change the method to accept a schematic instead
        Land land = arenaManager.generate(new Island(slot, new Island.IslandSchematic(new Location(world, 0, 0, 0, 0, 90), schematicFile)));
        Game game = new IslandSetup(new SetupPlayer(player), slot, schematicFile, land);

        playerLandMapping.put(player.id(), land);

        super.internalStart(player.id(), game);
    }

    @Override
    public void stop(UUID playerId) {
        super.stop(playerId);

        Land land = playerLandMapping.get(playerId);
        arenaManager.unlock(land);
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
        return new EndSetupState(islandService, lobbyService);
    }

    public void setOrigin(UUID playerId, Location location) {
        getSafe(playerId).dispatch(new SetOriginState(this, location));
    }

    static class BeginSetupState extends StartGameState {
        @Override
        public void apply(Game game) {
            apply((IslandSetup) game);
        }

        public void apply(IslandSetup game) {
            SetupPlayer gamePlayer = (SetupPlayer) game.gamePlayer();
            gamePlayer.player().teleport(game.land().getIslandLocation());
        }
    }

    static class SetOriginState implements Game.GameState {
        private final IslandSetupHandler setupHandler;
        private final Location origin;

        SetOriginState(IslandSetupHandler setupHandler, Location origin) {
            this.setupHandler = setupHandler;
            this.origin = origin;
        }

        @Override
        public void apply(Game game) {
            apply((IslandSetup) game);
        }

        public void apply(IslandSetup game) {
            Location subtracted = game.land().getIslandLocation().subtract(origin)
                    .setYaw(origin.getYaw()).setPitch(origin.getPitch());
            game.origin(subtracted);
            System.out.println("Setting island's setup location to " + subtracted);
            setupHandler.stop(game.gamePlayer().id());
        }

        @Override
        public boolean test(Game game) {
            System.out.println("gameState=" + game.gameState() + " (" + (game.gameState() instanceof StartGameState) + ")");
            return game.gameState() instanceof StartGameState;
        }
    }

    static class EndSetupState extends StopGameState {
        private final IslandService islandService;
        private final LobbyService lobbyService;

        EndSetupState(IslandService islandService, LobbyService lobbyService) {
            this.islandService = islandService;
            this.lobbyService = lobbyService;
        }

        @Override
        public void apply(Game game) {
            apply((IslandSetup) game);
        }

        public void apply(IslandSetup game) {
            if (game.origin() == null) return;
            try {
                islandService.register(game.slot(), game.origin(), game.schematicFile());
            } catch (Exception e) {
                e.printStackTrace();
            }

            SetupPlayer gamePlayer = (SetupPlayer) game.gamePlayer();
            gamePlayer.player().teleport(lobbyService.position());
        }
    }
}
