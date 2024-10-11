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

import com.mojang.datafixers.util.Pair;
import com.sparky.multirecipe.common.crafting.RecipeSelection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@SuppressWarnings("unused")
@Mixin(value = RecipeManager.class, priority = 900)
public class MixinRecipeManager {

    @Inject(
            at = @At("HEAD"),
            method = "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;",
            cancellable = true)
    private <C extends Container, T extends Recipe<C>> void multirecipe$getRecipe(
            RecipeType<T> recipeType, C inventory, Level level, ResourceLocation resourceLocation,
            CallbackInfoReturnable<Optional<Pair<ResourceLocation, RecipeHolder<T>>>> cb) {

        if (inventory instanceof BlockEntity) {
            RecipeSelection.getBlockEntityRecipe(recipeType, inventory, level, (BlockEntity) inventory)
                    .ifPresent(recipe -> cb.setReturnValue(Optional.of(Pair.of(resourceLocation, recipe))));
        }
    }
}
