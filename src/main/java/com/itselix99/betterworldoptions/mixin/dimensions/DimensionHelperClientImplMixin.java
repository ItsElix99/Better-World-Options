package com.itselix99.betterworldoptions.mixin.dimensions;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.modificationstation.stationapi.impl.client.world.dimension.DimensionHelperClientImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(DimensionHelperClientImpl.class)
public class DimensionHelperClientImplMixin {

    @ModifyArgs(
            method = "switchDimension",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;setPositionAndAnglesKeepPrevAngles(DDDFF)V",
                    ordinal = 1
            )
    )
    private void bwo_fixSpawnInFiniteWorld(Args args) {
        Minecraft minecraft = (Minecraft) FabricLoaderImpl.INSTANCE.getGameInstance();
        World world = minecraft.world;
        BWOProperties bwoProperties = (BWOProperties) world.getProperties();
        String worldType = bwoProperties.bwo_getWorldType();
        boolean finiteWorld = bwoProperties.bwo_getBooleanOptionValue("FiniteWorld", OptionType.GENERAL_OPTION);
        String finiteType = bwoProperties.bwo_getStringOptionValue("FiniteType", OptionType.GENERAL_OPTION);

        if (finiteWorld) {
            double sizeX = (double) bwoProperties.bwo_getIntOptionValue("SizeX", OptionType.GENERAL_OPTION) / 2;
            double sizeZ = (double) bwoProperties.bwo_getIntOptionValue("SizeZ", OptionType.GENERAL_OPTION) / 2;

            if (finiteType.equals("MCPE") || worldType.equals("Indev 223")) {
                if (worldType.equals("Early Infdev")) {
                    sizeX += world.random.nextDouble(-sizeX, sizeX);
                    sizeZ += world.random.nextDouble(-sizeX, sizeZ);
                } else {
                    sizeX += world.random.nextDouble(0, sizeX);
                    sizeZ += world.random.nextDouble(0, sizeZ);
                }
            } else {
                sizeX += world.random.nextDouble(-sizeX, sizeX);
                sizeZ += world.random.nextDouble(-sizeX, sizeZ);
            }

            args.set(0, sizeX);
            args.set(2, sizeZ);
        }
    }

    @ModifyArgs(
            method = "switchDimension",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;setPositionAndAnglesKeepPrevAngles(DDDFF)V",
                    ordinal = 2
            )
    )
    private void bwo_fixSpawnInFiniteWorld2(Args args) {
        Minecraft minecraft = (Minecraft) FabricLoaderImpl.INSTANCE.getGameInstance();
        World world = minecraft.world;
        BWOProperties bwoProperties = (BWOProperties) world.getProperties();
        String worldType = bwoProperties.bwo_getWorldType();
        boolean finiteWorld = bwoProperties.bwo_getBooleanOptionValue("FiniteWorld", OptionType.GENERAL_OPTION);
        String finiteType = bwoProperties.bwo_getStringOptionValue("FiniteType", OptionType.GENERAL_OPTION);

        if (finiteWorld && minecraft.player.dimensionId == 0) {
            double sizeX = (double) bwoProperties.bwo_getIntOptionValue("SizeX", OptionType.GENERAL_OPTION) / 2;
            double sizeZ = (double) bwoProperties.bwo_getIntOptionValue("SizeZ", OptionType.GENERAL_OPTION) / 2;

            if (finiteType.equals("MCPE") || worldType.equals("Indev 223")) {
                if (worldType.equals("Early Infdev")) {
                    sizeX += world.random.nextDouble(-sizeX, sizeX);
                    sizeZ += world.random.nextDouble(-sizeX, sizeZ);
                } else {
                    sizeX += world.random.nextDouble(0, sizeX);
                    sizeZ += world.random.nextDouble(0, sizeZ);
                }
            } else {
                sizeX += world.random.nextDouble(-sizeX, sizeX);
                sizeZ += world.random.nextDouble(-sizeX, sizeZ);
            }

            args.set(0, sizeX);
            args.set(2, sizeZ);
        }
    }
}