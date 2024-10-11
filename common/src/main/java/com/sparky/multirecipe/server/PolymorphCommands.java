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

package com.sparky.multirecipe.server;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.sparky.multirecipe.PolymorphConstants;
import com.sparky.multirecipe.mixin.core.AccessorSmithingTransformRecipe;
import com.sparky.multirecipe.mixin.core.AccessorSmithingTrimRecipe;
import com.sparky.multirecipe.platform.Services;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class PolymorphCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        final int opPermissionLevel = 2;
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("multirecipe")
                .requires(player -> player.hasPermission(opPermissionLevel));
        command.then(
                Commands.literal("conflicts").executes(context -> findConflicts(context.getSource())));
        dispatcher.register(command);
    }

    private static int findConflicts(CommandSourceStack source) {
        CompletableFuture.runAsync(() -> {
            source.sendSuccess(() -> Component.translatable("commands.multirecipe.conflicts.starting"),
                    true);
            ServerLevel world = source.getLevel();
            RecipeManager recipeManager = world.getRecipeManager();
            List<String> output = new ArrayList<>();
            int count = 0;
            count += scanRecipes(RecipeType.CRAFTING, output, recipeManager, CraftingRecipeWrapper::new);
            count += scanRecipes(RecipeType.SMELTING, output, recipeManager, RecipeWrapper::new);
            count += scanRecipes(RecipeType.BLASTING, output, recipeManager, RecipeWrapper::new);
            count += scanRecipes(RecipeType.SMOKING, output, recipeManager, RecipeWrapper::new);
            count += scanRecipes(RecipeType.SMITHING, output, recipeManager, SmithingRecipeWrapper::new);

            if (count > 0) {
                try {
                    Files.write(Paths.get(Services.PLATFORM.getGameDir() + "/logs/conflicts.log"), output,
                            StandardCharsets.UTF_8);
                } catch (IOException e) {
                    PolymorphConstants.LOG.error(
                            "Whoops! Something went wrong writing down your conflicts :(");
                    e.printStackTrace();
                }
            }
            int finalCount = count;
            source.sendSuccess(
                    () -> Component.translatable("commands.multirecipe.conflicts.success", finalCount), true);
        });
        return Command.SINGLE_SUCCESS;
    }

    private static <C extends Container, T extends Recipe<C>> int scanRecipes(RecipeType<T> pType,
                                                                              List<String> pOutput,
                                                                              RecipeManager pRecipeManager,
                                                                              Function<RecipeHolder<?>, RecipeWrapper> pFactory) {
        Collection<RecipeWrapper> recipes =
                pRecipeManager.getAllRecipesFor(pType).stream().map(pFactory).toList();
        List<Set<ResourceLocation>> conflicts = new ArrayList<>();
        Set<ResourceLocation> skipped = new TreeSet<>();
        Set<ResourceLocation> processed = new HashSet<>();

        for (RecipeWrapper recipe : recipes) {
            ResourceLocation id = recipe.id();

            if (processed.contains(id)) {
                continue;
            }
            processed.add(id);

            if (recipe.getRecipe().value() instanceof CustomRecipe) {
                skipped.add(id);
                continue;
            }
            Set<ResourceLocation> currentGroup = new TreeSet<>();

            for (RecipeWrapper otherRecipe : recipes) {

                if (processed.contains(otherRecipe.id())) {
                    continue;
                }

                if (otherRecipe.conflicts(recipe)) {
                    currentGroup.add(id);
                    currentGroup.add(otherRecipe.id());
                    processed.add(otherRecipe.id());
                }
            }

            if (!currentGroup.isEmpty()) {
                conflicts.add(currentGroup);
            }
        }
        pOutput.add("===================================================================");
        pOutput.add(
                BuiltInRegistries.RECIPE_TYPE.getKey(pType) + " recipe conflicts (" + conflicts.size() +
                        ")");
        pOutput.add("===================================================================");
        pOutput.add("");
        int count = 1;

        for (Set<ResourceLocation> conflict : conflicts) {
            StringJoiner joiner = new StringJoiner(", ");
            conflict.stream().map(ResourceLocation::toString).forEach(joiner::add);
            pOutput.add(count + ": " + joiner);
            pOutput.add("");
            count++;
        }

        if (skipped.size() > 0) {
            pOutput.add("Skipped special recipes: ");

            for (ResourceLocation resourceLocation : skipped) {
                pOutput.add(resourceLocation.toString());
            }
            pOutput.add("");
        }
        return conflicts.size();
    }

    private static class IngredientWrapper {

        private final Ingredient ingredient;

        private IngredientWrapper(Ingredient pIngredient) {
            this.ingredient = pIngredient;
        }

        public Ingredient getIngredient() {
            return this.ingredient;
        }

        public boolean matches(IngredientWrapper pIngredient) {

            if (pIngredient == null) {
                return false;
            }
            Ingredient otherIngredient = pIngredient.getIngredient();

            if (otherIngredient == null) {
                return false;
            } else if (otherIngredient == Ingredient.EMPTY) {
                return this.ingredient == Ingredient.EMPTY;
            } else {
                ItemStack[] stacks = this.ingredient.getItems();

                for (ItemStack otherStack : pIngredient.getIngredient().getItems()) {

                    for (ItemStack stack : stacks) {

                        if (ItemStack.matches(stack, otherStack)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }
    }

    private static class RecipeWrapper {

        private final RecipeHolder<?> recipe;
        private final List<IngredientWrapper> ingredients;

        private RecipeWrapper(RecipeHolder<?> pRecipe) {
            this.recipe = pRecipe;
            this.ingredients = new ArrayList<>();

            for (Ingredient ingredient : this.recipe.value().getIngredients()) {
                IngredientWrapper wrapped = new IngredientWrapper(ingredient);
                this.ingredients.add(wrapped);
            }
        }

        public RecipeHolder<?> getRecipe() {
            return this.recipe;
        }

        public ResourceLocation id() {
            return this.recipe.id();
        }

        public List<IngredientWrapper> getIngredients() {
            return this.ingredients;
        }

        public boolean conflicts(RecipeWrapper pOther) {

            if (pOther == null) {
                return false;
            } else if (this.id().equals(pOther.id())) {
                return true;
            } else if (this.ingredients.size() != pOther.getIngredients().size()) {
                return false;
            } else {
                List<IngredientWrapper> otherIngredients = pOther.getIngredients();

                for (int i = 0; i < otherIngredients.size(); i++) {

                    if (!otherIngredients.get(i).matches(this.getIngredients().get(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    private static class SmithingRecipeWrapper extends RecipeWrapper {

        private SmithingRecipeWrapper(RecipeHolder<?> pRecipe) {
            super(pRecipe);
        }

        @Override
        public boolean conflicts(RecipeWrapper pOther) {
            Ingredient template = Ingredient.EMPTY;
            Ingredient base = Ingredient.EMPTY;
            Ingredient addition = Ingredient.EMPTY;
            Ingredient otherTemplate = Ingredient.EMPTY;
            Ingredient otherBase = Ingredient.EMPTY;
            Ingredient otherAddition = Ingredient.EMPTY;
            RecipeHolder<?> recipe = this.getRecipe();
            RecipeHolder<?> otherRecipe = pOther.getRecipe();

            if (otherRecipe.value() instanceof SmithingTrimRecipe) {
                AccessorSmithingTrimRecipe accessorSmithingRecipe = (AccessorSmithingTrimRecipe) recipe.value();
                template = accessorSmithingRecipe.getTemplate();
                base = accessorSmithingRecipe.getBase();
                addition = accessorSmithingRecipe.getAddition();
            } else if (recipe.value() instanceof SmithingTrimRecipe ||
                    recipe.value() instanceof SmithingTransformRecipe) {
                AccessorSmithingTransformRecipe accessorSmithingRecipe =
                        (AccessorSmithingTransformRecipe) recipe.value();
                template = accessorSmithingRecipe.getTemplate();
                base = accessorSmithingRecipe.getBase();
                addition = accessorSmithingRecipe.getAddition();
            }

            if (otherRecipe.value() instanceof SmithingTrimRecipe) {
                AccessorSmithingTrimRecipe accessorSmithingRecipe =
                        (AccessorSmithingTrimRecipe) otherRecipe.value();
                otherTemplate = accessorSmithingRecipe.getTemplate();
                otherBase = accessorSmithingRecipe.getBase();
                otherAddition = accessorSmithingRecipe.getAddition();
            } else if (otherRecipe.value() instanceof SmithingTransformRecipe) {
                AccessorSmithingTransformRecipe accessorSmithingRecipe =
                        (AccessorSmithingTransformRecipe) otherRecipe.value();
                otherTemplate = accessorSmithingRecipe.getTemplate();
                otherBase = accessorSmithingRecipe.getBase();
                otherAddition = accessorSmithingRecipe.getAddition();
            }
            IngredientWrapper baseWrapper = new IngredientWrapper(base);
            IngredientWrapper otherBaseWrapper = new IngredientWrapper(otherBase);
            IngredientWrapper additionWrapper = new IngredientWrapper(addition);
            IngredientWrapper otherAdditionWrapper = new IngredientWrapper(otherAddition);
            IngredientWrapper templateWrapper = new IngredientWrapper(template);
            IngredientWrapper otherTemplateWrapper = new IngredientWrapper(otherTemplate);
            return super.conflicts(pOther) &&
                    baseWrapper.matches(otherBaseWrapper) & additionWrapper.matches(otherAdditionWrapper) &&
                    templateWrapper.matches(otherTemplateWrapper);
        }
    }

    private static class CraftingRecipeWrapper extends RecipeWrapper {

        private final boolean shaped;

        private CraftingRecipeWrapper(RecipeHolder<?> recipe) {
            super(recipe);
            this.shaped = Services.PLATFORM.isShaped(recipe);
        }

        public boolean isShaped() {
            return this.shaped;
        }

        @Override
        public boolean conflicts(RecipeWrapper pOther) {
            return super.conflicts(pOther) && isSameShape((CraftingRecipeWrapper) pOther);
        }

        private boolean isSameShape(CraftingRecipeWrapper other) {

            if (this.shaped && other.isShaped()) {
                return Services.PLATFORM.isSameShape(this.getRecipe(), other.getRecipe());
            }
            return true;
        }
    }
}
