package com.itselix99.betterworldoptions.mixin.network;

import com.itselix99.betterworldoptions.world.WorldGenerationOptions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.network.NetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientNetworkHandler.class)
public abstract class ClientNetworkHandlerMixin extends NetworkHandler {

    @Inject(method = "<init>", at = @At("TAIL"))
    private static void newWorldGenerationOptions(Minecraft address, String port, int par3, CallbackInfo ci){
        new WorldGenerationOptions();
    }
}