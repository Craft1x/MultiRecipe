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

package com.sparky.multirecipe;

import com.sparky.multirecipe.common.CommonEventsListener;
import com.sparky.multirecipe.common.PolymorphFabricNetwork;
import com.sparky.multirecipe.common.components.PolymorphFabricComponents;
import com.sparky.multirecipe.server.PolymorphCommands;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class PolymorphFabricMod implements ModInitializer {

  @Override
  public void onInitialize() {
    PolymorphCommonMod.init();
    PolymorphCommonMod.setup();
    PolymorphFabricNetwork.setup();
    PolymorphFabricComponents.setup();
    CommandRegistrationCallback.EVENT.register(
        (dispatcher, registryAccess, environment) -> PolymorphCommands.register(dispatcher));
    CommonEventsListener.setup();
  }
}
