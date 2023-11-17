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

import com.mojang.datafixers.util.Pair;
import com.sparky.multirecipe.api.PolymorphApi;
import com.sparky.multirecipe.api.common.base.IRecipePair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nonnull;
import java.util.SortedSet;

public abstract class AbstractHighlightedRecipeData<E extends BlockEntity>
        extends AbstractBlockEntityRecipeData<E> {

    public AbstractHighlightedRecipeData(E owner) {
        super(owner);
    }

    @Override
    public void selectRecipe(@Nonnull RecipeHolder<?> recipe) {
        super.selectRecipe(recipe);

        for (ServerPlayer listeningPlayer : this.getListeners()) {
            PolymorphApi.common().getPacketDistributor()
                    .sendHighlightRecipeS2C(listeningPlayer, recipe.id());
        }
    }

    @Override
    public Pair<SortedSet<IRecipePair>, ResourceLocation> getPacketData() {
        SortedSet<IRecipePair> recipesList = this.getRecipesList();
        ResourceLocation selected = null;

        if (!recipesList.isEmpty()) {
            selected = this.getSelectedRecipe().map(RecipeHolder::id)
                    .orElse(recipesList.first().getResourceLocation());
        }
        return new Pair<>(recipesList, selected);
    }
}
