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
import java.util.SortedSet;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class PolymorphFabricPacketDistributor implements IPolymorphPacketDistributor {

  @Override
  public void sendPlayerRecipeSelectionC2S(ResourceLocation resourceLocation) {
    FriendlyByteBuf buf = PacketByteBufs.create();
    CPacketPlayerRecipeSelection.encode(new CPacketPlayerRecipeSelection(resourceLocation), buf);
    ClientPlayNetworking.send(PolymorphFabricNetwork.PLAYER_SELECT, buf);
  }

  @Override
  public void sendPersistentRecipeSelectionC2S(ResourceLocation resourceLocation) {
    FriendlyByteBuf buf = PacketByteBufs.create();
    CPacketPersistentRecipeSelection.encode(new CPacketPersistentRecipeSelection(resourceLocation),
        buf);
    ClientPlayNetworking.send(PolymorphFabricNetwork.PERSISTENT_SELECT, buf);
  }

  @Override
  public void sendStackRecipeSelectionC2S(ResourceLocation resourceLocation) {
    FriendlyByteBuf buf = PacketByteBufs.create();
    CPacketStackRecipeSelection.encode(new CPacketStackRecipeSelection(resourceLocation), buf);
    ClientPlayNetworking.send(PolymorphFabricNetwork.STACK_SELECT, buf);
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
    FriendlyByteBuf buf = PacketByteBufs.create();
    SPacketRecipesList.encode(new SPacketRecipesList(recipesList, selected), buf);
    ServerPlayNetworking.send(player, PolymorphFabricNetwork.RECIPES_LIST, buf);
  }

  @Override
  public void sendHighlightRecipeS2C(ServerPlayer player, ResourceLocation resourceLocation) {
    FriendlyByteBuf buf = PacketByteBufs.create();
    SPacketHighlightRecipe.encode(new SPacketHighlightRecipe(resourceLocation), buf);
    ServerPlayNetworking.send(player, PolymorphFabricNetwork.HIGHLIGHT_RECIPE, buf);
  }

  @Override
  public void sendPlayerSyncS2C(ServerPlayer player, SortedSet<IRecipePair> recipesList,
                                ResourceLocation selected) {
    FriendlyByteBuf buf = PacketByteBufs.create();
    SPacketPlayerRecipeSync.encode(new SPacketPlayerRecipeSync(recipesList, selected), buf);
    ServerPlayNetworking.send(player, PolymorphFabricNetwork.RECIPE_SYNC, buf);
  }

  @Override
  public void sendBlockEntitySyncS2C(BlockPos blockPos, ResourceLocation selected) {
    FriendlyByteBuf buf = PacketByteBufs.create();
    SPacketBlockEntityRecipeSync.encode(new SPacketBlockEntityRecipeSync(blockPos, selected), buf);
    PolymorphApi.common().getServer().ifPresent(server -> PlayerLookup.all(server).forEach(
        player -> ServerPlayNetworking.send(player, PolymorphFabricNetwork.BLOCK_ENTITY_SYNC,
            buf)));
  }

  @Override
  public void sendBlockEntityListenerC2S(boolean add) {
    FriendlyByteBuf buf = PacketByteBufs.create();
    CPacketBlockEntityListener.encode(new CPacketBlockEntityListener(add), buf);
    ClientPlayNetworking.send(PolymorphFabricNetwork.BLOCK_ENTITY_LISTENER, buf);
  }
}
