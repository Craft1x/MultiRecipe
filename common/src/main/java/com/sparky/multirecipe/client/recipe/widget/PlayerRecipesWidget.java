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

package com.sparky.multirecipe.client.recipe.widget;

import com.sparky.multirecipe.api.PolymorphApi;
import com.sparky.multirecipe.api.client.widget.AbstractRecipesWidget;
import com.sparky.multirecipe.api.common.base.IPolymorphCommon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public class PlayerRecipesWidget extends AbstractRecipesWidget {

    final Slot outputSlot;

    public PlayerRecipesWidget(AbstractContainerScreen<?> containerScreen, Slot outputSlot) {
        super(containerScreen);
        this.outputSlot = outputSlot;
    }

    @Override
    public void selectRecipe(ResourceLocation resourceLocation) {
        IPolymorphCommon commonApi = PolymorphApi.common();
        Player player = Minecraft.getInstance().player;

        if (player != null) {
            player.level().getRecipeManager().byKey(resourceLocation).ifPresent(
                    recipe -> commonApi.getRecipeData(player)
                            .ifPresent(recipeData -> recipeData.selectRecipe(recipe)));
        }
        commonApi.getPacketDistributor().sendPlayerRecipeSelectionC2S(resourceLocation);
    }

    @Override
    public Slot getOutputSlot() {
        return this.outputSlot;
    }
}
