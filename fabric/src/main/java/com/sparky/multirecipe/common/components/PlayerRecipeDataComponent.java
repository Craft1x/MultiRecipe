/*
 * Copyright (C) 2020-2023 Sparky
 *
 * MultiRecipe is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * MultiRecipe is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with MultiRecipe.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.sparky.multirecipe.common.components;

import com.sparky.multirecipe.common.capability.PlayerRecipeData;
import dev.onyxstudios.cca.api.v3.component.Component;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class PlayerRecipeDataComponent extends PlayerRecipeData implements Component {

  public PlayerRecipeDataComponent(Player owner) {
    super(owner);
  }

  @Override
  public void readFromNbt(@Nonnull CompoundTag tag) {
    this.readNBT(tag.getCompound("Data"));
  }

  @Override
  public void writeToNbt(@Nonnull CompoundTag tag) {
    tag.put("Data", this.writeNBT());
  }
}
