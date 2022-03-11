package io.tofpu.speedbridge2.support.placeholderapi.expansion;

import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.support.placeholderapi.expansion.expansions.TotalStatsExpansion;
import java.util.HashMap;
import java.util.Map;

public final class ExpansionHandler {
  public static final ExpansionHandler INSTANCE = new ExpansionHandler();
  //    private static final String[] CUSTOM_STATS = {"total_wins", "total_tries"};

  private final Map<String, AbstractExpansion> expansionMap;

  private ExpansionHandler() {
    this.expansionMap = new HashMap<>();
  }

  public void load() {
    final String[] customStats = {"total_wins", "total_tries"};
    for (final String stats : customStats) {
      final AbstractExpansion abstractExpansion = new TotalStatsExpansion(stats);
      this.expansionMap.put(stats, abstractExpansion);
    }
  }

  public AbstractExpansion register(final AbstractExpansion abstractExpansion) {
    expansionMap.put(abstractExpansion.getIdentifier(), abstractExpansion);
    return abstractExpansion;
  }

  public String match(final BridgePlayer bridgePlayer, final String[] args) {
    //        final String joinedArg = String.join("_", args).replace("speedbridge_", "");
    final String joinedArg = String.join("_", args);
    for (final AbstractExpansion abstractExpansion : expansionMap.values()) {
      final String identifier = abstractExpansion.getIdentifier();
      // if the identifier is not identical with the joined args, or if it
      // doesn't pass the requirements, continue through the collection
      if (!joinedArg.contains(identifier)
          || !abstractExpansion.passedRequirement(bridgePlayer, args)) {
        continue;
      }

      // running the action
      final String result =
          abstractExpansion.runAction(bridgePlayer, bridgePlayer.getGamePlayer(), args);

      // if the result were empty, return the default action
      if (result.isEmpty()) {
        return abstractExpansion.getDefaultAction(bridgePlayer);
      }
      // otherwise, return the result
      return result;
    }

    // return an empty string if there was no identical placeholder
    return "";
  }
}
