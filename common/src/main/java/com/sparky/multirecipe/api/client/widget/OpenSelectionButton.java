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

import com.mojang.blaze3d.systems.RenderSystem;
import com.sparky.multirecipe.platform.Services;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

import static com.sparky.multirecipe.PolymorphConstants.MOD_ID;

public class OpenSelectionButton extends ImageButton {

    public static final WidgetSprites BUTTON_SPRITES = new WidgetSprites(new ResourceLocation(MOD_ID, "widget/button"), new ResourceLocation(MOD_ID, "widget/button_highlighted"));
    private final AbstractContainerScreen<?> containerScreen;
    private int xOffset;
    private int yOffset;


    public OpenSelectionButton(AbstractContainerScreen<?> containerScreen, int x, int y,
                               OnPress onPress) {
        super(0, 0, 16, 16, BUTTON_SPRITES, onPress);
        this.containerScreen = containerScreen;
        this.xOffset = x;
        this.yOffset = y;
    }


    public void setOffsets(int x, int y) {
        this.xOffset = x;
        this.yOffset = y;
    }

    @Override
    public void renderWidget(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY,
                             float partialTicks) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.setX(Services.CLIENT_PLATFORM.getScreenLeft(this.containerScreen) + this.xOffset);
        this.setY(Services.CLIENT_PLATFORM.getScreenTop(this.containerScreen) + this.yOffset);
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTicks);
    }
}
