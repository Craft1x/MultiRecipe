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

package com.sparky.multirecipe.api.client.base;

import com.sparky.multirecipe.api.client.widget.SelectionWidget;
import com.sparky.multirecipe.api.common.base.IRecipePair;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;

import java.util.Set;

public interface IRecipesWidget {

    void initChildWidgets();

    void selectRecipe(ResourceLocation resourceLocation);

    void highlightRecipe(ResourceLocation resourceLocation);

    void setRecipesList(Set<IRecipePair> recipesList, ResourceLocation selected);

    void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float renderPartialTicks);

    boolean mouseClicked(double mouseX, double mouseY, int button);

    Slot getOutputSlot();

    SelectionWidget getSelectionWidget();

    int getButtonXPos();

    int getButtonYPos();
}
