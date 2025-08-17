package com.itselix99.betterworldoptions.mixin.network;

import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.network.WorldGenerationOptionsPacket;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.login.LoginHelloPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.SERVER)
@Mixin(ServerLoginNetworkHandler.class)
public abstract class ServerLoginNetworkHandlerMixin extends NetworkHandler {
    @Shadow private MinecraftServer server;

    @Inject(
            method = "accept",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V",
                    ordinal = 0
            )
    )
    private void worldGenerationOptionsPacket(LoginHelloPacket packet, CallbackInfo ci, @Local ServerPlayNetworkHandler var5){
        World world = this.server.getWorld(0);
        BWOProperties properties = (BWOProperties) world.getProperties();
        var5.sendPacket(new WorldGenerationOptionsPacket(properties));
    }
}