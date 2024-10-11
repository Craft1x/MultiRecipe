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

import com.sparky.multirecipe.mixin.core.AccessorAbstractFurnaceBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

public class FurnaceRecipeData extends AbstractHighlightedRecipeData<AbstractFurnaceBlockEntity> {

    public FurnaceRecipeData(AbstractFurnaceBlockEntity owner) {
        super(owner);
    }

    @Override
    protected NonNullList<ItemStack> getInput() {

        if (((AccessorAbstractFurnaceBlockEntity) this.getOwner()).getItems() != null) {
            return NonNullList.of(ItemStack.EMPTY, this.getOwner().getItem(0));
        } else {
            return NonNullList.create();
        }
    }

    @Override
    public boolean isEmpty() {
        return this.getInput().get(0).isEmpty();
    }
}
