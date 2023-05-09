package io.tofpu.speedbridge2.command.subcommand.debug;

import com.sk89q.worldedit.WorldEditException;
import io.tofpu.speedbridge2.model.island.IslandFactory;
import io.tofpu.speedbridge2.model.island.arena.ArenaManager;
import io.tofpu.speedbridge2.model.island.object.GameIsland;
import io.tofpu.speedbridge2.model.island.object.Island;
import io.tofpu.speedbridge2.model.island.object.land.IslandLand;
import io.tofpu.speedbridge2.model.player.object.GamePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.*;

@Command("speedbridge debug")
@CommandPermission("speedbridge.debug")
public class SpeedBridgeDebugCommand {
    private final Map<Island, List<GameIsland>> generatedGames = new HashMap<>();
    private final ArenaManager arenaManager;

    public SpeedBridgeDebugCommand(ArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    @Subcommand("arena teleport")
    public String islandTeleport(final Player player) {
        int[] positions = arenaManager.getPositions();
        player.teleport(new Location(this.arenaManager.getWorld(), positions[0], positions[1], positions[2]));
        return String.format("Teleported you to world %s with %s coordinates", arenaManager.getWorld().getName(), positions);
    }

    @Subcommand("island generate")
    @Usage("island generate <island> [amount]")
    public String generateGame(final Island island, final @Default("1") int amount) {
        for (int i = 0; i < amount; i++) {
            generateGame(island);
        }
        return String.format("Generated %s games of island type %s", amount, island.getSlot());
    }

    @Subcommand("island destroy")
    @Usage("island destroy")
    public String islandClear(final Island island) {
        int gameAmountDestroyed = destroyGames(island);
        int islandType = island.getSlot();
        return String.format("Destroyed %s games of island type %s!", gameAmountDestroyed, islandType);
    }

    private int destroyGames(final Island island) {
        List<GameIsland> games = this.generatedGames.get(island);
        games.forEach(game -> {
            DestroyableLand islandPlot = (DestroyableLand) game.getIslandPlot();
            islandPlot.destroy();
        });
        return games.size();
    }

    public void generateGame(final Island island) {
        final GamePlayer gamePlayer = GamePlayer.of(new EmptyBridgePlayer(UUID.randomUUID()));
        final GameIsland2 gameIsland = new GameIsland2(arenaManager, IslandFactory.createGame(island, gamePlayer));

        DestroyableLand islandLand = new DestroyableLand(arenaManager.justReservePlot(gameIsland));
        try {
            islandLand.generatePlot();
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }

        gameIsland.setIslandPlot(islandLand);

        this.generatedGames.compute(island, (island1, gameIslands) -> {
            if (gameIslands == null) {
                gameIslands = new ArrayList<>();
            }
            gameIslands.add(gameIsland);
            return gameIslands;
        });
    }
}
