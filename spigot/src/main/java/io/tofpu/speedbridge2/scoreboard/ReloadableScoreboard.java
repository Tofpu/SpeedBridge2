package io.tofpu.speedbridge2.scoreboard;

import io.tofpu.speedbridge2.reload.domain.Reloadable;

public class ReloadableScoreboard implements Reloadable {
    private final ScoreboardSystem scoreboardSystem;

    public ReloadableScoreboard(ScoreboardSystem scoreboardSystem) {
        this.scoreboardSystem = scoreboardSystem;
    }

    @Override
    public void onReload() {
        scoreboardSystem.reloadScoreboards();
        System.out.println("Scoreboards reloaded successfully.");
    }

    @Override
    public Key key() {
        return Key.of("scoreboard");
    }
}
