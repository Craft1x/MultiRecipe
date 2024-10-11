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

package com.sparky.multirecipe.common;

import com.sparky.multirecipe.api.PolymorphApi;
import com.sparky.multirecipe.api.common.capability.IBlockEntityRecipeData;
import com.sparky.multirecipe.api.common.capability.IPlayerRecipeData;
import com.sparky.multirecipe.api.common.capability.IStackRecipeData;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class PolymorphForgeCapabilities {

  public static final Capability<IPlayerRecipeData> PLAYER_RECIPE_DATA =
      CapabilityManager.get(new CapabilityToken<>() {
      });
  public static final Capability<IBlockEntityRecipeData> BLOCK_ENTITY_RECIPE_DATA =
      CapabilityManager.get(new CapabilityToken<>() {
      });
  public static final Capability<IStackRecipeData> STACK_RECIPE_DATA =
      CapabilityManager.get(new CapabilityToken<>() {
      });

  public static final ResourceLocation PLAYER_RECIPE_DATA_ID =
      new ResourceLocation(PolymorphApi.MOD_ID, "player_recipe_data");
  public static final ResourceLocation BLOCK_ENTITY_RECIPE_DATA_ID =
      new ResourceLocation(PolymorphApi.MOD_ID, "block_entity_recipe_data");
  public static final ResourceLocation STACK_RECIPE_DATA_ID =
      new ResourceLocation(PolymorphApi.MOD_ID, "stack_recipe_data");
}
