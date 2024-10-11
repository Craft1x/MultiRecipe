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

import com.mojang.datafixers.util.Pair;
import com.sparky.multirecipe.api.PolymorphApi;
import com.sparky.multirecipe.api.common.base.IPolymorphCommon;
import com.sparky.multirecipe.api.common.base.IPolymorphPacketDistributor;
import com.sparky.multirecipe.api.common.base.IRecipePair;
import com.sparky.multirecipe.common.util.BlockEntityTicker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;

import java.util.SortedSet;

public class PolymorphCommonEvents {

    public static void levelTick(Level level) {

        if (!level.isClientSide() && level.getGameTime() % 5 == 0) {
            BlockEntityTicker.tick();
        }
    }

    public static void playerDisconnected(ServerPlayer serverPlayer) {
        BlockEntityTicker.remove(serverPlayer);
    }

    public static void openContainer(Player player, AbstractContainerMenu containerMenu) {

        if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayerEntity) {
            IPolymorphCommon commonApi = PolymorphApi.common();
            commonApi.getRecipeDataFromBlockEntity(containerMenu).ifPresent(
                    recipeData -> {
                        IPolymorphPacketDistributor packetDistributor = commonApi.getPacketDistributor();

                        if (recipeData.isFailing() || recipeData.isEmpty(null)) {
                            packetDistributor.sendRecipesListS2C(serverPlayerEntity);
                        } else {
                            Pair<SortedSet<IRecipePair>, ResourceLocation> data = recipeData.getPacketData();
                            packetDistributor.sendRecipesListS2C(serverPlayerEntity, data.getFirst(),
                                    data.getSecond());
                        }
                    });
        }
    }
}
