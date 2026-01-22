package com.itselix99.betterworldoptions.mixin.dimensions;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.modificationstation.stationapi.impl.server.world.dimension.DimensionHelperServerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(DimensionHelperServerImpl.class)
public class DimensionHelperServerImplMixin {

    @ModifyArgs(
            method = "switchDimension",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/ServerPlayerEntity;setPositionAndAnglesKeepPrevAngles(DDDFF)V"
            )
    )
    private void bwo_fixSpawnInFiniteWorld(Args args, @Local(ordinal = 0) ServerPlayerEntity serverPlayer) {
        MinecraftServer minecraftServer = (MinecraftServer) FabricLoaderImpl.INSTANCE.getGameInstance();
        World world = minecraftServer.getWorld(0);
        BWOProperties bwoProperties = (BWOProperties) world.getProperties();
        boolean finiteWorld = bwoProperties.bwo_getBooleanOptionValue("FiniteWorld", OptionType.GENERAL_OPTION);

        if (finiteWorld && serverPlayer.dimensionId == 0) {
            double sizeX = (double) bwoProperties.bwo_getIntOptionValue("SizeX", OptionType.GENERAL_OPTION) / 2;
            double sizeZ = (double) bwoProperties.bwo_getIntOptionValue("SizeZ", OptionType.GENERAL_OPTION) / 2;
            sizeX += world.random.nextDouble(0, sizeX);
            sizeZ += world.random.nextDouble(0, sizeZ);

            args.set(0, sizeX);
            args.set(2, sizeZ);
        }
    }
}