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

package com.sparky.multirecipe.common.impl;

import com.sparky.multirecipe.api.common.base.IPolymorphCommon;
import com.sparky.multirecipe.api.common.base.IPolymorphPacketDistributor;
import com.sparky.multirecipe.api.common.capability.IBlockEntityRecipeData;
import com.sparky.multirecipe.api.common.capability.IPlayerRecipeData;
import com.sparky.multirecipe.api.common.capability.IStackRecipeData;
import com.sparky.multirecipe.common.capability.PolymorphCapabilities;
import com.sparky.multirecipe.common.util.BlockEntityTicker;
import com.sparky.multirecipe.platform.Services;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class PolymorphCommon implements IPolymorphCommon {

    private static final IPolymorphCommon INSTANCE = new PolymorphCommon();
    private final List<IBlockEntity2RecipeData> blockEntity2RecipeData = new LinkedList<>();
    private final List<IContainer2BlockEntity> container2BlockEntities = new LinkedList<>();
    private final List<IContainer2ItemStack> container2ItemStacks = new LinkedList<>();
    private final List<IItemStack2RecipeData> itemStack2RecipeData = new LinkedList<>();
    private final IPolymorphPacketDistributor distributor = Services.PLATFORM.getPacketDistributor();
    private MinecraftServer server = null;

    public static IPolymorphCommon get() {
        return INSTANCE;
    }

    @Override
    public IPolymorphPacketDistributor getPacketDistributor() {
        return distributor;
    }

    @Override
    public Optional<MinecraftServer> getServer() {
        return Optional.ofNullable(this.server);
    }

    @Override
    public void setServer(MinecraftServer server) {
        this.server = server;
        BlockEntityTicker.clear();
    }

    @Override
    public Optional<? extends IBlockEntityRecipeData> tryCreateRecipeData(BlockEntity blockEntity) {

        for (IBlockEntity2RecipeData function : this.blockEntity2RecipeData) {
            IBlockEntityRecipeData recipeData = function.createRecipeData(blockEntity);

            if (recipeData != null) {
                return Optional.of(recipeData);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<? extends IBlockEntityRecipeData> getRecipeData(BlockEntity blockEntity) {
        return PolymorphCapabilities.getRecipeData(blockEntity);
    }

    @Override
    public Optional<? extends IBlockEntityRecipeData> getRecipeDataFromBlockEntity(
            AbstractContainerMenu container) {

        for (IContainer2BlockEntity function : this.container2BlockEntities) {
            BlockEntity blockEntity = function.getBlockEntity(container);

            if (blockEntity != null) {
                return this.getRecipeData(blockEntity);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<? extends IStackRecipeData> tryCreateRecipeData(ItemStack stack) {

        for (IItemStack2RecipeData function : this.itemStack2RecipeData) {
            IStackRecipeData recipeData = function.createRecipeData(stack);

            if (recipeData != null) {
                return Optional.of(recipeData);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<? extends IStackRecipeData> getRecipeData(ItemStack stack) {
        return PolymorphCapabilities.getRecipeData(stack);
    }

    @Override
    public Optional<? extends IStackRecipeData> getRecipeDataFromItemStack(
            AbstractContainerMenu pContainer) {

        for (IContainer2ItemStack function : this.container2ItemStacks) {
            ItemStack itemstack = function.getItemStack(pContainer);

            if (!itemstack.isEmpty()) {
                return this.getRecipeData(itemstack);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<? extends IPlayerRecipeData> getRecipeData(Player player) {
        return PolymorphCapabilities.getRecipeData(player);
    }

    @Override
    public void registerBlockEntity2RecipeData(
            IBlockEntity2RecipeData blockEntity2RecipeData) {
        this.blockEntity2RecipeData.add(blockEntity2RecipeData);
    }

    @Override
    public void registerContainer2BlockEntity(IContainer2BlockEntity container2BlockEntity) {
        this.container2BlockEntities.add(container2BlockEntity);
    }

    @Override
    public void registerItemStack2RecipeData(IItemStack2RecipeData stack2RecipeData) {
        this.itemStack2RecipeData.add(stack2RecipeData);
    }

    @Override
    public void registerContainer2ItemStack(IContainer2ItemStack container2ItemStack) {
        this.container2ItemStacks.add(container2ItemStack);
    }
}
