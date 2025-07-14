package io.tofpu.speedbridge2.game.domain;

import io.tofpu.speedbridge2.util.ColorUtil;
import io.tofpu.speedbridge2.util.placeholder.Placeholder;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public record GameFeedback(
        Sound sound,
        Title title,
        List<String> commands,
        List<String> messages
) {
        public void apply(Player player, Placeholder... placeholders) {
                player.playSound(sound);
                player.showTitle(title);
                commands.forEach(command -> {
                        command = Placeholder.replaceAll(command, placeholders);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                });
                // todo: we need to parse the placeholders in the messages
                messages.forEach(message -> {
                        message = Placeholder.replaceAll(message, placeholders);
                        player.sendMessage(ColorUtil.colorize(message));
                });
        }
}
