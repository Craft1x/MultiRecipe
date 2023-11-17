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

package com.sparky.multirecipe.common;

import com.sparky.multirecipe.api.PolymorphApi;
import com.sparky.multirecipe.common.network.client.CPacketBlockEntityListener;
import com.sparky.multirecipe.common.network.client.CPacketPersistentRecipeSelection;
import com.sparky.multirecipe.common.network.client.CPacketPlayerRecipeSelection;
import com.sparky.multirecipe.common.network.client.CPacketStackRecipeSelection;
import com.sparky.multirecipe.common.network.server.SPacketBlockEntityRecipeSync;
import com.sparky.multirecipe.common.network.server.SPacketHighlightRecipe;
import com.sparky.multirecipe.common.network.server.SPacketPlayerRecipeSync;
import com.sparky.multirecipe.common.network.server.SPacketRecipesList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class PolymorphForgeNetwork {

    private static final Integer PTC_VERSION = 1;

    private static SimpleChannel instance;
    private static int id = 0;

    public static SimpleChannel get() {
        return instance;
    }

    public static void setup() {
        instance = ChannelBuilder.named(new ResourceLocation(PolymorphApi.MOD_ID, "main")).simpleChannel();


        // Server-to-Client
        registerS2C(SPacketRecipesList.class, SPacketRecipesList::encode, SPacketRecipesList::decode,
                SPacketRecipesList::handle);
        registerS2C(SPacketHighlightRecipe.class, SPacketHighlightRecipe::encode,
                SPacketHighlightRecipe::decode, SPacketHighlightRecipe::handle);
        registerS2C(SPacketPlayerRecipeSync.class, SPacketPlayerRecipeSync::encode,
                SPacketPlayerRecipeSync::decode, SPacketPlayerRecipeSync::handle);
        registerS2C(SPacketBlockEntityRecipeSync.class, SPacketBlockEntityRecipeSync::encode,
                SPacketBlockEntityRecipeSync::decode, SPacketBlockEntityRecipeSync::handle);

        // Client-to-Server
        registerC2S(CPacketPlayerRecipeSelection.class, CPacketPlayerRecipeSelection::encode,
                CPacketPlayerRecipeSelection::decode, CPacketPlayerRecipeSelection::handle);
        registerC2S(CPacketPersistentRecipeSelection.class, CPacketPersistentRecipeSelection::encode,
                CPacketPersistentRecipeSelection::decode, CPacketPersistentRecipeSelection::handle);
        registerC2S(CPacketStackRecipeSelection.class, CPacketStackRecipeSelection::encode,
                CPacketStackRecipeSelection::decode, CPacketStackRecipeSelection::handle);
        registerC2S(CPacketBlockEntityListener.class, CPacketBlockEntityListener::encode,
                CPacketBlockEntityListener::decode, CPacketBlockEntityListener::handle);
    }

    public static <M> void registerC2S(Class<M> clazz, BiConsumer<M, FriendlyByteBuf> encoder,
                                       Function<FriendlyByteBuf, M> decoder,
                                       BiConsumer<M, ServerPlayer> handler) {

        instance.messageBuilder(clazz, NetworkDirection.PLAY_TO_SERVER).encoder(encoder).decoder(decoder).consumerMainThread((message, contextSupplier) -> {
            ServerPlayer sender = contextSupplier.getSender();

            if (sender != null) {
                handler.accept(message, sender);
            }
        }).add();


    }

    public static <M> void registerS2C(Class<M> clazz, BiConsumer<M, FriendlyByteBuf> encoder,
                                       Function<FriendlyByteBuf, M> decoder, Consumer<M> handler) {
        instance.messageBuilder(clazz, NetworkDirection.PLAY_TO_CLIENT).encoder(encoder).decoder(decoder).consumerMainThread((message, contextSupplier) -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handler.accept(message));
        }).add();

    }
}
