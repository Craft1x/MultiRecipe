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

package com.sparky.multirecipe.api.common.base;

import com.sparky.multirecipe.api.common.capability.IBlockEntityRecipeData;
import com.sparky.multirecipe.api.common.capability.IPlayerRecipeData;
import com.sparky.multirecipe.api.common.capability.IStackRecipeData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

public interface IPolymorphCommon {

    Optional<? extends IBlockEntityRecipeData> tryCreateRecipeData(BlockEntity blockEntity);

    Optional<? extends IBlockEntityRecipeData> getRecipeData(BlockEntity blockEntity);

    Optional<? extends IBlockEntityRecipeData> getRecipeDataFromBlockEntity(
            AbstractContainerMenu containerMenu);

    Optional<? extends IStackRecipeData> tryCreateRecipeData(ItemStack stack);

    Optional<? extends IStackRecipeData> getRecipeData(ItemStack stack);

    Optional<? extends IStackRecipeData> getRecipeDataFromItemStack(AbstractContainerMenu container);

    Optional<? extends IPlayerRecipeData> getRecipeData(Player player);

    void registerBlockEntity2RecipeData(IBlockEntity2RecipeData blockEntity2RecipeData);

    void registerContainer2BlockEntity(IContainer2BlockEntity container2BlockEntity);

    void registerItemStack2RecipeData(IItemStack2RecipeData stack2RecipeData);

    void registerContainer2ItemStack(IContainer2ItemStack container2ItemStack);

    IPolymorphPacketDistributor getPacketDistributor();

    Optional<MinecraftServer> getServer();

    void setServer(MinecraftServer pServer);

    interface IItemStack2RecipeData {

        IStackRecipeData createRecipeData(ItemStack stack);
    }

    interface IBlockEntity2RecipeData {

        IBlockEntityRecipeData createRecipeData(BlockEntity blockEntity);
    }

    interface IContainer2BlockEntity {

        BlockEntity getBlockEntity(AbstractContainerMenu containerMenu);
    }

    interface IContainer2ItemStack {

        ItemStack getItemStack(AbstractContainerMenu containerMenu);
    }
}
