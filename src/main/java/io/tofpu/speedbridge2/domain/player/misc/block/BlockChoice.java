package io.tofpu.speedbridge2.domain.player.misc.block;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public interface BlockChoice {
  Material getChoseMaterial();

  void setChosenMaterial(final @NotNull Material material);
}
