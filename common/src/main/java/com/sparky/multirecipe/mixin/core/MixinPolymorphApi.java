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

package com.sparky.multirecipe.mixin.core;

import com.sparky.multirecipe.api.PolymorphApi;
import com.sparky.multirecipe.api.client.base.IPolymorphClient;
import com.sparky.multirecipe.api.common.base.IPolymorphCommon;
import com.sparky.multirecipe.client.impl.PolymorphClient;
import com.sparky.multirecipe.common.impl.PolymorphCommon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(PolymorphApi.class)
public class MixinPolymorphApi {

    @Inject(
            at = @At("HEAD"),
            method = "common",
            remap = false,
            cancellable = true
    )
    private static void multirecipe$common(CallbackInfoReturnable<IPolymorphCommon> cir) {
        cir.setReturnValue(PolymorphCommon.get());
    }

    @Inject(
            at = @At("HEAD"),
            method = "client",
            remap = false,
            cancellable = true
    )
    private static void multirecipe$client(CallbackInfoReturnable<IPolymorphClient> cir) {
        cir.setReturnValue(PolymorphClient.get());
    }
}
