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

import com.mojang.blaze3d.vertex.PoseStack;
import com.sparky.multirecipe.api.common.base.IRecipePair;
import com.sparky.multirecipe.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class SelectionWidget implements Renderable, GuiEventListener {

    private final Consumer<ResourceLocation> onSelect;
    private final AbstractContainerScreen<?> containerScreen;
    private final List<OutputWidget> outputWidgets = new ArrayList<>();
    private int xOffset;
    private int yOffset;

    private OutputWidget hoveredButton;
    private boolean active = false;
    private int x;
    private int y;
    private int lastX;
    private int lastY;

    public SelectionWidget(int x, int y, int xOffset, int yOffset,
                           Consumer<ResourceLocation> onSelect,
                           AbstractContainerScreen<?> containerScreen) {
        this.setPosition(x, y);
        this.onSelect = onSelect;
        this.containerScreen = containerScreen;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        this.updateButtonPositions();
    }

    public void setOffsets(int x, int y) {
        this.xOffset = x;
        this.yOffset = y;
    }

    public void highlightButton(ResourceLocation resourceLocation) {
        this.outputWidgets.forEach(
                widget -> widget.setHighlighted(widget.getResourceLocation().equals(resourceLocation)));
    }

    private void updateButtonPositions() {
        int size = this.outputWidgets.size();
        int yOffset = -25 * size / 2;

      //  if (size % 2 == 0) {
            yOffset += 11;
       // }
        int[] pos = {this.x, this.y + yOffset};
        this.outputWidgets.forEach(widget -> {
            widget.setPosition(pos[0], pos[1]);
            pos[1] += 25;
        });
    }

    public List<OutputWidget> getOutputWidgets() {
        return outputWidgets;
    }

    public void setRecipeList(Set<IRecipePair> recipeList) {
        this.outputWidgets.clear();
        recipeList.forEach(data -> {
            if (!data.getOutput().isEmpty()) {
                this.outputWidgets.add(new OutputWidget(data));
            }
        });
        this.updateButtonPositions();
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.screen != null && this.hoveredButton != null) {
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            poseStack.translate(0, 0, 501);
            guiGraphics.renderTooltip(mc.font, this.hoveredButton.getOutput(), mouseX, mouseY);
            poseStack.popPose();
        }
    }

    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {

        if (this.isActive()) {
            int x = Services.CLIENT_PLATFORM.getScreenLeft(this.containerScreen) + this.xOffset;
            int y = Services.CLIENT_PLATFORM.getScreenTop(this.containerScreen) + this.yOffset;

            if (this.lastX != x || this.lastY != y) {
                this.setPosition(x, y);
                this.lastX = x;
                this.lastY = y;
            }
            this.hoveredButton = null;
            this.outputWidgets.forEach(button -> {
                button.render(guiGraphics, mouseX, mouseY, partialTicks);

                if (button.visible && button.isHoveredOrFocused()) {
                    this.hoveredButton = button;
                }
            });
            this.renderTooltip(guiGraphics, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        if (this.isActive()) {

            for (OutputWidget widget : this.outputWidgets) {

                if (widget.mouseClicked(mouseX, mouseY, button)) {
                    onSelect.accept(widget.getResourceLocation());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public void setFocused(boolean var1) {

    }
}
