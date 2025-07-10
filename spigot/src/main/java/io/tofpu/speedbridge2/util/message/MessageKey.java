package io.tofpu.speedbridge2.util.message;

import io.tofpu.speedbridge2.util.ColorUtil;
import org.bukkit.command.CommandSender;

public interface MessageKey {
    String key();
    String component();

    default PreparedMessage of(Object... args) {
        return new PreparedMessage(MessagesUtil.format(component(), key(), args));
    }

    class PreparedMessage {
        private final String message;

        public PreparedMessage(String message) {
            this.message = ColorUtil.colorize(message);;
        }

        public void send(CommandSender sender) {
            sender.sendMessage(message);
        }

        public String toMessage() {
            return message;
        }

        @Override
        public String toString() {
            return message;
        }
    }
}
