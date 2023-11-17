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

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;

import java.util.List;

public class FurnaceRecipesWidget extends PersistentRecipesWidget {

    private final Slot outputSlot;

    public FurnaceRecipesWidget(AbstractContainerScreen<?> containerScreen) {
        super(containerScreen);
        List<Slot> slots = containerScreen.getMenu().slots;
        this.outputSlot = slots.get(2);
    }

    @Override
    public Slot getOutputSlot() {
        return this.outputSlot;
    }

    @Override
    public int getButtonXOffset() {
        return 36;
    }
    @Override
    public int getWidgetXOffset() {
        return 24;
    }

}
