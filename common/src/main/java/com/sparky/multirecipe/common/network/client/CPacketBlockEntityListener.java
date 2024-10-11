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

package com.sparky.multirecipe.common.network.client;

import com.sparky.multirecipe.api.PolymorphApi;
import com.sparky.multirecipe.common.util.BlockEntityTicker;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public record CPacketBlockEntityListener(boolean add) {

    public static void encode(CPacketBlockEntityListener packet, FriendlyByteBuf buffer) {
        buffer.writeBoolean(packet.add);
    }

    public static CPacketBlockEntityListener decode(FriendlyByteBuf buffer) {
        return new CPacketBlockEntityListener(buffer.readBoolean());
    }

    public static void handle(CPacketBlockEntityListener packet, ServerPlayer player) {

        if (player != null) {

            if (packet.add) {
                AbstractContainerMenu container = player.containerMenu;
                PolymorphApi.common().getRecipeDataFromBlockEntity(container)
                        .ifPresent(recipeData -> BlockEntityTicker.add(player, recipeData));
            } else {
                BlockEntityTicker.remove(player);
            }
        }
    }
}
