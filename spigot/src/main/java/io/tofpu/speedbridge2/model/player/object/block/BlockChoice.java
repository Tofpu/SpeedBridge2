package io.tofpu.speedbridge2.model.player.object.block;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public interface BlockChoice {
    /**
     * Returns the material that the user has chosen
     *
     * @return The material that was chosen by the user.
     */
    Material getChoseMaterial();

    /**
     * Set the material of the object to the given material
     *
     * @param material The material to set the block to.
     */
    void setChosenMaterial(final @NotNull Material material);
}
