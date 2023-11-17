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

package com.sparky.multirecipe.client.recipe;

import com.mojang.datafixers.util.Pair;
import com.sparky.multirecipe.api.PolymorphApi;
import com.sparky.multirecipe.api.client.base.IRecipesWidget;
import com.sparky.multirecipe.api.common.base.IRecipePair;
import com.sparky.multirecipe.client.recipe.widget.PersistentRecipesWidget;
import com.sparky.multirecipe.client.recipe.widget.PlayerRecipesWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.SortedSet;

public class RecipesWidget {

    private static IRecipesWidget widget = null;
    private static Screen lastScreen = null;
    private static Pair<SortedSet<IRecipePair>, ResourceLocation> pendingData = null;

    public static Optional<IRecipesWidget> get() {
        return Optional.ofNullable(widget);
    }

    public static void enqueueRecipesList(SortedSet<IRecipePair> recipesList,
                                          ResourceLocation resourceLocation) {
        pendingData = new Pair<>(recipesList, resourceLocation);
    }

    public static void create(AbstractContainerScreen<?> containerScreen) {

        if (containerScreen == lastScreen && widget != null) {
            return;
        }
        widget = null;
        Optional<IRecipesWidget> maybeWidget = PolymorphApi.client().getWidget(containerScreen);
        maybeWidget.ifPresent(newWidget -> widget = newWidget);

        if (widget == null) {
            PolymorphApi.client().findCraftingResultSlot(containerScreen)
                    .ifPresent(slot -> widget = new PlayerRecipesWidget(containerScreen, slot));
        }

        if (widget != null) {

            if (widget instanceof PersistentRecipesWidget &&
                    Minecraft.getInstance().getConnection() != null) {
                PolymorphApi.common().getPacketDistributor().sendBlockEntityListenerC2S(true);
            }
            widget.initChildWidgets();
            lastScreen = containerScreen;

            if (pendingData != null) {
                widget.setRecipesList(pendingData.getFirst(), pendingData.getSecond());
            }
        } else {
            lastScreen = null;
        }
        pendingData = null;
    }

    public static void clear() {

        if (widget instanceof PersistentRecipesWidget &&
                Minecraft.getInstance().getConnection() != null) {
            PolymorphApi.common().getPacketDistributor().sendBlockEntityListenerC2S(false);
        }
        widget = null;
        lastScreen = null;
    }
}
