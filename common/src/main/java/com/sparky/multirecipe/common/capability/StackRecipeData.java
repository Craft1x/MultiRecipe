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

import com.sparky.multirecipe.api.PolymorphApi;
import com.sparky.multirecipe.api.common.base.IPolymorphCommon;
import com.sparky.multirecipe.api.common.capability.IStackRecipeData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class StackRecipeData extends AbstractRecipeData<ItemStack> implements IStackRecipeData {

    public StackRecipeData(ItemStack owner) {
        super(owner);
    }

    @Override
    public Set<ServerPlayer> getListeners() {
        Set<ServerPlayer> players = new HashSet<>();
        IPolymorphCommon commonApi = PolymorphApi.common();
        commonApi.getServer().ifPresent(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                commonApi.getRecipeDataFromItemStack(player.containerMenu)
                        .ifPresent(recipeData -> {
                            if (recipeData == this) {
                                players.add(player);
                            }
                        });
            }
        });
        return players;
    }
}
