package io.tofpu.speedbridge2.model.common.toolbar;

import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public final class SerializableToolbarItem {
    private int index;

    private ItemStack item;
    private ToolAction action;

    public SerializableToolbarItem() {
    }

    public SerializableToolbarItem(final int index, final ItemStack item, final ToolAction action) {
        this.index = index;
        this.item = item;
        this.action = action;
    }

    public int getIndex() {
        return index;
    }

    public ItemStack getItem() {
        return item;
    }

    public ToolAction getAction() {
        return action;
    }

    @ConfigSerializable
    public static final class ToolAction {

        public enum ActionType {
            PERFORM_COMMAND
        }

        private ActionType type;
        @Setting("run_command") // to maintain compatibility with older versions
        private String value;

        public ToolAction() {
        }

        public ToolAction(ActionType type, String value) {
            this.type = type;
            this.value = value;
        }

        public ActionType type() {
            return type;
        }

        public void setType(ActionType type) {
            this.type = type;
        }

        public String value() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
