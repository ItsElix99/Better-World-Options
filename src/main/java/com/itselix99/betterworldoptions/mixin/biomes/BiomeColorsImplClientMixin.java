package com.itselix99.betterworldoptions.mixin.biomes;

import com.itselix99.betterworldoptions.api.worldtype.OldFeaturesProperties;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.impl.worldgen.BiomeColorsImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(BiomeColorsImpl.class)
public class BiomeColorsImplClientMixin {

    @ModifyReturnValue(
            method = "lambda$static$0",
            at = @At(
                    value = "RETURN",
                    ordinal = 0
            )
    )
    private static int bwo_modifyGrassColor(int original) {
        Minecraft minecraft = (Minecraft) FabricLoaderImpl.INSTANCE.getGameInstance();
        String worldType = ((BWOProperties) minecraft.world.getProperties()).bwo_getWorldType();
        boolean oldFeatures = ((BWOProperties) minecraft.world.getProperties()).bwo_isOldFeatures();

        if (Config.BWOConfig.environment.oldTexturesAndSky && oldFeatures && worldType.equals("MCPE") && minecraft.world.dimension.id == 0) {
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
    private static int bwo_modifyLeavesColor(int original) {
        Minecraft minecraft = (Minecraft) FabricLoaderImpl.INSTANCE.getGameInstance();
        String worldType = ((BWOProperties) minecraft.world.getProperties()).bwo_getWorldType();
        boolean oldFeatures = ((BWOProperties) minecraft.world.getProperties()).bwo_isOldFeatures();

        if (Config.BWOConfig.environment.oldTexturesAndSky && oldFeatures && worldType.equals("MCPE") && minecraft.world.dimension.id == 0) {
            return 6396257;
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
    private static int bwo_modifyFogColor(int original) {
        Minecraft minecraft = (Minecraft) FabricLoaderImpl.INSTANCE.getGameInstance();
        String worldType = ((BWOProperties) minecraft.world.getProperties()).bwo_getWorldType();
        boolean oldFeatures = ((BWOProperties) minecraft.world.getProperties()).bwo_isOldFeatures();
        String theme = ((BWOProperties) minecraft.world.getProperties()).bwo_getTheme();
        OldFeaturesProperties oldFeaturesProperties = WorldTypes.getOldFeaturesProperties(worldType);

        if (minecraft.world.dimension.id == 0) {
            if (Config.BWOConfig.environment.oldTexturesAndSky && oldFeatures && oldFeaturesProperties != null && oldFeaturesProperties.oldFeaturesBiomeSupplier.get() == null && oldFeaturesProperties.defaultFogColor != -1 && (theme.equals("Normal") || theme.equals("Winter"))) {
                return oldFeaturesProperties.defaultFogColor;
            } else if (theme.equals("Hell")) {
                return 1049600;
            } else if (theme.equals("Paradise")) {
                return 13033215;
            } else if (theme.equals("Woods")) {
                return 5069403;
            }
        }

        return original;
    }
}