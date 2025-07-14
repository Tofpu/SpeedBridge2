package io.tofpu.speedbridge2.scoreboard.infra.config;

import space.arim.dazzleconf.annote.ConfDefault;

import java.util.Collection;
import java.util.List;

public interface ScoreboardConfiguration {
    @ConfDefault.DefaultObject("defaultLines")
    List<String> lines();

    static Collection<String> defaultLines() {
        return List.of(
                "&fWelcome to &bSpeedBridge&f!",
                "",
                "&a» &fYour goal: ",
                " &eBridge as fast as possible",
                "",
                "&a» &fPersonal Best: &b%personal_best%",
                "&a» &fCurrent Time: &e%sb_current_time%",
                "",
                "&7Stay sharp. Stay fast."
        );
    }
}
