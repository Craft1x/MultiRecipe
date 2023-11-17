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

package com.sparky.multirecipe.platform;

import com.sparky.multirecipe.PolymorphConstants;
import com.sparky.multirecipe.platform.services.IClientPlatform;
import com.sparky.multirecipe.platform.services.IPlatform;

import java.util.ServiceLoader;

public class Services {

    public static final IClientPlatform CLIENT_PLATFORM = load(IClientPlatform.class);
    public static final IPlatform PLATFORM = load(IPlatform.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(
                        () -> new NullPointerException("Failed to load service for " + clazz.getName()));
        PolymorphConstants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
