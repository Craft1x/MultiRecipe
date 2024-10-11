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

package com.sparky.multirecipe.api.client.widget;

import com.sparky.multirecipe.api.PolymorphApi;
import com.sparky.multirecipe.api.client.base.IRecipesWidget;
import com.sparky.multirecipe.api.common.base.IRecipePair;
import com.sparky.multirecipe.platform.Services;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class AbstractRecipesWidget implements IRecipesWidget {

    public static final ResourceLocation WIDGETS =
            new ResourceLocation(PolymorphApi.MOD_ID, "textures/gui/widgets.png");

    protected final AbstractContainerScreen<?> containerScreen;

    protected SelectionWidget selectionWidget;
    protected OpenSelectionButton openButton;

    public AbstractRecipesWidget(AbstractContainerScreen<?> containerScreen) {
        this.containerScreen = containerScreen;
    }


    @Override
    public void initChildWidgets() {
        int x = Services.CLIENT_PLATFORM.getScreenLeft(this.containerScreen) + this.getButtonXPos();
        int y = Services.CLIENT_PLATFORM.getScreenTop(this.containerScreen) + this.getButtonYPos();
        this.selectionWidget =
                new SelectionWidget(x + getWidgetXOffset(), y + getWidgetYOffset(), this.getButtonXPos() + getWidgetXOffset(),
                        this.getButtonYPos() + getWidgetYOffset(), this::selectRecipe, this.containerScreen);
        this.openButton = new OpenSelectionButton(this.containerScreen, this.getButtonXPos(), this.getButtonYPos(),
                clickWidget -> this.selectionWidget.setActive(!this.selectionWidget.isActive()));
        this.openButton.visible = this.selectionWidget.getOutputWidgets().size() > 1;
    }


    @Override
    public abstract void selectRecipe(ResourceLocation resourceLocation);

    @Override
    public SelectionWidget getSelectionWidget() {
        return selectionWidget;
    }

    @Override
    public void highlightRecipe(ResourceLocation resourceLocation) {
        this.selectionWidget.highlightButton(resourceLocation);
    }

    @Override
    public void setRecipesList(Set<IRecipePair> recipesList, ResourceLocation selected) {
        SortedSet<IRecipePair> sorted = new TreeSet<>(recipesList);
        this.selectionWidget.setRecipeList(sorted);
        this.openButton.visible = recipesList.size() > 1;

        if (selected != null) {
            this.highlightRecipe(selected);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float renderPartialTicks) {
        this.selectionWidget.render(guiGraphics, mouseX, mouseY, renderPartialTicks);
        this.openButton.render(guiGraphics, mouseX, mouseY, renderPartialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.openButton.mouseClicked(mouseX, mouseY, button)) {
            return true;
        } else if (this.selectionWidget.mouseClicked(mouseX, mouseY, button)) {
            this.selectionWidget.setActive(false);
            return true;
        } else if (this.selectionWidget.isActive()) {

            if (!this.openButton.mouseClicked(mouseX, mouseY, button)) {
                this.selectionWidget.setActive(false);
            }
            return true;
        }
        return false;
    }

    @Override
    public int getButtonXPos() {
        return this.getOutputSlot().x + getButtonXOffset();
    }

    @Override
    public int getButtonYPos() {
        return this.getOutputSlot().y + getButtonYOffset();
    }

    public int getButtonXOffset() {
        return 28;
    }

    public int getButtonYOffset() {
        return 28;
    }

    public int getWidgetXOffset() {
        return 25;
    }

    public int getWidgetYOffset() {
        return -3;
    }
}
