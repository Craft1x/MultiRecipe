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

package com.sparky.multirecipe.mixin.core;

import com.sparky.multirecipe.common.crafting.RecipeSelection;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@SuppressWarnings("unused")
@Mixin(CraftingMenu.class)
public class MixinCraftingMenu {

    @Redirect(
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/world/item/crafting/RecipeManager.getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;"),
            method = "slotChangedCraftingGrid")
    private static <C extends Container, T extends Recipe<C>> Optional<RecipeHolder<T>> multirecipe$getRecipe(
            RecipeManager recipeManager, RecipeType<T> type, C inventory, Level world,
            AbstractContainerMenu p_150547_, Level p_150548_, Player player,
            CraftingContainer p_150550_, ResultContainer p_150551_) {
        return RecipeSelection.getPlayerRecipe(p_150547_, type, inventory, world, player);
    }
}
