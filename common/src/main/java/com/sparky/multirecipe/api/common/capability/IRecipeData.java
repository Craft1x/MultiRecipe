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

package com.sparky.multirecipe.api.common.capability;

import com.mojang.datafixers.util.Pair;
import com.sparky.multirecipe.api.common.base.IRecipePair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;

public interface IRecipeData<E> {


    <T extends Recipe<C>, C extends Container> Optional<RecipeHolder<T>> getRecipe(RecipeType<T> type,
                                                                                   C inventory, Level level,
                                                                                   List<RecipeHolder<T>> recipesList);

    void selectRecipe(@Nonnull RecipeHolder<?> recipe);

    Optional<? extends RecipeHolder<?>> getSelectedRecipe();

    void setSelectedRecipe(@Nonnull RecipeHolder<?> recipe);

    @Nonnull
    SortedSet<IRecipePair> getRecipesList();

    void setRecipesList(@Nonnull SortedSet<IRecipePair> recipesList);

    boolean isEmpty(Container container);

    Set<ServerPlayer> getListeners();

    void sendRecipesListToListeners(boolean empty);

    Pair<SortedSet<IRecipePair>, ResourceLocation> getPacketData();

    E getOwner();

    boolean isFailing();

    void setFailing(boolean failing);

    @Nonnull
    CompoundTag writeNBT();

    void readNBT(CompoundTag compound);
}
