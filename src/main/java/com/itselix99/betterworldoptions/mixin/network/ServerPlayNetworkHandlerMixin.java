package com.itselix99.betterworldoptions.mixin.network;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.NetworkHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Environment(EnvType.SERVER)
@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin extends NetworkHandler {
    @Shadow private MinecraftServer server;

    @ModifyArgs(
            method = "onPlayerRespawn",
            at = @At
                    (
                            value = "INVOKE",
                            target = "Lnet/minecraft/server/PlayerManager;respawnPlayer(Lnet/minecraft/entity/player/ServerPlayerEntity;I)Lnet/minecraft/entity/player/ServerPlayerEntity;"
                    )
    )
    private void bwo_respawnInOtherDimensions(Args args){
        World world = this.server.getWorld(0);
        BWOProperties bwoProperties = (BWOProperties) world.getProperties();
        String worldType = bwoProperties.bwo_getWorldType();
        boolean skyDimension = bwoProperties.bwo_getBooleanOptionValue("SkyDimension", OptionType.WORLD_TYPE_OPTION);

        if (worldType.equals("Nether")) {
            args.set(1, -1);
        } else if (worldType.equals("Skylands") && skyDimension) {
            args.set(1, 1);
        } else if (WorldTypes.getWorldTypeByName(worldType).isDimension) {
            args.set(1, WorldTypes.getWorldTypeByName(worldType).dimensionId);
        }
    }
}