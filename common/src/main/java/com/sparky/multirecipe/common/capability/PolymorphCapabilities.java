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

package com.sparky.multirecipe.common.capability;

import com.sparky.multirecipe.api.common.capability.IBlockEntityRecipeData;
import com.sparky.multirecipe.api.common.capability.IPlayerRecipeData;
import com.sparky.multirecipe.api.common.capability.IStackRecipeData;
import com.sparky.multirecipe.platform.Services;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

public class PolymorphCapabilities {

    public static Optional<? extends IPlayerRecipeData> getRecipeData(Player player) {
        return Services.PLATFORM.getRecipeData(player);
    }

    public static Optional<? extends IBlockEntityRecipeData> getRecipeData(BlockEntity blockEntity) {
        return Services.PLATFORM.getRecipeData(blockEntity);
    }

    public static Optional<? extends IStackRecipeData> getRecipeData(ItemStack stack) {
        return Services.PLATFORM.getRecipeData(stack);
    }
}
