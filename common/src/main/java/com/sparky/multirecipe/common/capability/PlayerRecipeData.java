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
import com.sparky.multirecipe.api.common.capability.IPlayerRecipeData;
import com.sparky.multirecipe.client.recipe.RecipesWidget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.*;

public class PlayerRecipeData extends AbstractRecipeData<Player> implements
        IPlayerRecipeData {

    private AbstractContainerMenu containerMenu;

    public PlayerRecipeData(Player owner) {
        super(owner);
    }

    @Override
    public <T extends Recipe<C>, C extends Container> Optional<RecipeHolder<T>> getRecipe(RecipeType<T> type,
                                                                                          C inventory,
                                                                                          Level level,
                                                                                          List<RecipeHolder<T>> recipesList) {
        Optional<RecipeHolder<T>> maybeRecipe = super.getRecipe(type, inventory, level, recipesList);

        if (this.getContainerMenu() == this.getOwner().containerMenu) {
            this.syncPlayerRecipeData();
        }
        this.setContainerMenu(null);
        return maybeRecipe;
    }

    @Override
    public void selectRecipe(@Nonnull RecipeHolder<?> recipe) {
        super.selectRecipe(recipe);
        this.syncPlayerRecipeData();
    }

    private void syncPlayerRecipeData() {

        if (this.getOwner() instanceof ServerPlayer) {
            PolymorphApi.common().getPacketDistributor()
                    .sendPlayerSyncS2C((ServerPlayer) this.getOwner(), this.getRecipesList(),
                            this.getSelectedRecipe().map(RecipeHolder::id).orElse(null));
        }
    }

    @Override
    public void sendRecipesListToListeners(boolean isEmpty) {

        if (this.getContainerMenu() == this.getOwner().containerMenu) {
            Pair<SortedSet<IRecipePair>, ResourceLocation> packetData =
                    isEmpty ? new Pair<>(new TreeSet<>(), null) : this.getPacketData();
            Player player = this.getOwner();

            if (player.level().isClientSide()) {
                RecipesWidget.get().ifPresent(
                        widget -> widget.setRecipesList(packetData.getFirst(), packetData.getSecond()));
            } else if (player instanceof ServerPlayer) {
                PolymorphApi.common().getPacketDistributor()
                        .sendRecipesListS2C((ServerPlayer) player, packetData.getFirst(),
                                packetData.getSecond());
            }
        }
    }

    @Override
    public Set<ServerPlayer> getListeners() {
        Player player = this.getOwner();

        if (player instanceof ServerPlayer) {
            return Collections.singleton((ServerPlayer) player);
        } else {
            return new HashSet<>();
        }
    }

    @Override
    public AbstractContainerMenu getContainerMenu() {
        return this.containerMenu;
    }

    @Override
    public void setContainerMenu(AbstractContainerMenu containerMenu) {
        this.containerMenu = containerMenu;
    }
}
