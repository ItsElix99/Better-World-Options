package com.itselix99.betterworldoptions.mixin.biomes;

import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.impl.worldgen.BiomeColorsImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BiomeColorsImpl.class)
public class BiomeColorsMixin {

    @ModifyReturnValue(
            method = "lambda$static$0",
            at = @At(
                    value = "RETURN",
                    ordinal = 0
            )
    )
    private static int modifyGrassColor(int original) {
        @Deprecated Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
        String worldType = ((BWOProperties) minecraft.world.getProperties()).bwo_getWorldType();
        boolean betaFeatures = ((BWOProperties) minecraft.world.getProperties()).bwo_getBetaFeatures();

        if (!betaFeatures && worldType.equals("MCPE")) {
            return 3381555;
        }

        return original;
    }

    @ModifyReturnValue(
            method = "lambda$static$1",
            at = @At(
                    value = "RETURN",
                    ordinal = 0
            )
    )
    private static int modifyLeavesColor(int original) {
        @Deprecated Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
        String worldType = ((BWOProperties) minecraft.world.getProperties()).bwo_getWorldType();
        boolean betaFeatures = ((BWOProperties) minecraft.world.getProperties()).bwo_getBetaFeatures();

        if (!betaFeatures && worldType.equals("MCPE")) {
            return 16777215;
        }

        return original;
    }

    @ModifyReturnValue(
            method = "lambda$static$2",
            at = @At(
                    value = "RETURN",
                    ordinal = 0
            )
    )
    private static int modifyFogColor(int original) {
        @Deprecated Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
        String worldType = ((BWOProperties) minecraft.world.getProperties()).bwo_getWorldType();
        boolean betaFeatures = ((BWOProperties) minecraft.world.getProperties()).bwo_getBetaFeatures();

        if (!betaFeatures && worldType.equals("MCPE")) {
            return 6731007;
        }

        return original;
    }
}