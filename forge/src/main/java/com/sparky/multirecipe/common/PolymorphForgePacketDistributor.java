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

import com.sparky.multirecipe.api.common.base.IPolymorphPacketDistributor;
import com.sparky.multirecipe.api.common.base.IRecipePair;
import com.sparky.multirecipe.common.network.client.CPacketBlockEntityListener;
import com.sparky.multirecipe.common.network.client.CPacketPersistentRecipeSelection;
import com.sparky.multirecipe.common.network.client.CPacketPlayerRecipeSelection;
import com.sparky.multirecipe.common.network.client.CPacketStackRecipeSelection;
import com.sparky.multirecipe.common.network.server.SPacketBlockEntityRecipeSync;
import com.sparky.multirecipe.common.network.server.SPacketHighlightRecipe;
import com.sparky.multirecipe.common.network.server.SPacketPlayerRecipeSync;
import com.sparky.multirecipe.common.network.server.SPacketRecipesList;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

import java.util.SortedSet;

public class PolymorphForgePacketDistributor implements IPolymorphPacketDistributor {

    @Override
    public void sendPlayerRecipeSelectionC2S(ResourceLocation resourceLocation) {
        PolymorphForgeNetwork.get().send(new CPacketPlayerRecipeSelection(resourceLocation), PacketDistributor.SERVER.noArg());
    }

    @Override
    public void sendPersistentRecipeSelectionC2S(ResourceLocation resourceLocation) {
        PolymorphForgeNetwork.get().send(new CPacketPersistentRecipeSelection(resourceLocation), PacketDistributor.SERVER.noArg());
    }

    @Override
    public void sendStackRecipeSelectionC2S(ResourceLocation resourceLocation) {
        PolymorphForgeNetwork.get().send(new CPacketStackRecipeSelection(resourceLocation), PacketDistributor.SERVER.noArg());
    }

    @Override
    public void sendRecipesListS2C(ServerPlayer player) {
        sendRecipesListS2C(player, null);
    }

    @Override
    public void sendRecipesListS2C(ServerPlayer player, SortedSet<IRecipePair> recipesList) {
        sendRecipesListS2C(player, recipesList, null);
    }

    @Override
    public void sendRecipesListS2C(ServerPlayer player, SortedSet<IRecipePair> recipesList,
                                   ResourceLocation selected) {
        PolymorphForgeNetwork.get().send(new SPacketRecipesList(recipesList, selected), PacketDistributor.PLAYER.with(player));
    }

    @Override
    public void sendHighlightRecipeS2C(ServerPlayer player, ResourceLocation pResourceLocation) {
        PolymorphForgeNetwork.get().send(new SPacketHighlightRecipe(pResourceLocation), PacketDistributor.PLAYER.with(player));
    }

    @Override
    public void sendPlayerSyncS2C(ServerPlayer player, SortedSet<IRecipePair> recipesList,
                                  ResourceLocation selected) {
        PolymorphForgeNetwork.get().send(new SPacketPlayerRecipeSync(recipesList, selected), PacketDistributor.PLAYER.with(player));
    }

    @Override
    public void sendBlockEntitySyncS2C(BlockPos blockPos, ResourceLocation selected) {
        PolymorphForgeNetwork.get().send(new SPacketBlockEntityRecipeSync(blockPos, selected), PacketDistributor.ALL.noArg());
    }

    @Override
    public void sendBlockEntityListenerC2S(boolean add) {
        PolymorphForgeNetwork.get()
                .send(new CPacketBlockEntityListener(add), PacketDistributor.SERVER.noArg());
    }
}
