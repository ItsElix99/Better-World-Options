package com.itselix99.betterworldoptions.mixin.network;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.network.BWOWorldPropertiesStoragePacket;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.Connection;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.handshake.HandshakePacket;
import net.minecraft.network.packet.login.LoginHelloPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
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
    @Shadow public Connection connection;

    @Inject(method = "onHandshake", at = @At("TAIL"))
    private void bwo_sendBWOWorldPropertiesStoragePacket(HandshakePacket packet, CallbackInfo ci){
        World world = this.server.getWorld(0);
        BWOProperties bwoProperties = (BWOProperties) world.getProperties();
        this.connection.sendPacket(new BWOWorldPropertiesStoragePacket(bwoProperties));
    }

    @Inject(
            method = "accept",
            at = @At
                    (
                            value = "INVOKE",
                            target = "Lnet/minecraft/server/PlayerManager;loadPlayerData(Lnet/minecraft/entity/player/ServerPlayerEntity;)V"
                    )
    )
    private void bwo_startInOtherDimensions(LoginHelloPacket var1, CallbackInfo ci, @Local ServerPlayerEntity player){
        World world = this.server.getWorld(0);
        BWOProperties bwoProperties = (BWOProperties) world.getProperties();
        String worldType = bwoProperties.bwo_getWorldType();
        boolean skyDimension = bwoProperties.bwo_getBooleanOptionValue("SkyDimension", OptionType.WORLD_TYPE_OPTION);

        if (worldType.equals("Nether")) {
            player.dimensionId = -1;
        } else if (worldType.equals("Skylands") && skyDimension) {
            player.dimensionId = 1;
        } else if (WorldTypes.getWorldTypeByName(worldType).isDimension) {
            player.dimensionId = WorldTypes.getWorldTypeByName(worldType).dimensionId;
        }
    }
}