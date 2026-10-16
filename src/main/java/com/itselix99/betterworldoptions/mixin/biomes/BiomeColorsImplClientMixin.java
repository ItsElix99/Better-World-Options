package com.itselix99.betterworldoptions.mixin.biomes;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.theme.Theme;
import com.itselix99.betterworldoptions.api.worldtype.OldFeaturesProperties;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
        Minecraft minecraft = BetterWorldOptions.getMinecraft();
        WorldType worldType = ((BWOWorld) minecraft.world).bwo_getWorldType();
        boolean oldFeatures = ((BWOProperties) minecraft.world.getProperties()).bwo_isOldFeatures();
        OldFeaturesProperties oldFeaturesProperties = worldType.getOldFeaturesProperties();

        if (Config.BWOConfig.environment.oldTexturesAndSky && oldFeatures && oldFeaturesProperties != null && oldFeaturesProperties.oldBiome == null && oldFeaturesProperties.defaultGrassColor != null && minecraft.world.dimension.id == 0) {
            return oldFeaturesProperties.defaultGrassColor.getColor(null, 0, 0);
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
        Minecraft minecraft = BetterWorldOptions.getMinecraft();
        WorldType worldType = ((BWOWorld) minecraft.world).bwo_getWorldType();
        boolean oldFeatures = ((BWOProperties) minecraft.world.getProperties()).bwo_isOldFeatures();
        OldFeaturesProperties oldFeaturesProperties = worldType.getOldFeaturesProperties();

        if (Config.BWOConfig.environment.oldTexturesAndSky && oldFeatures && oldFeaturesProperties != null && oldFeaturesProperties.oldBiome == null && oldFeaturesProperties.defaultLeavesColor != null && minecraft.world.dimension.id == 0) {
            return oldFeaturesProperties.defaultLeavesColor.getColor(null, 0, 0);
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
        Minecraft minecraft = BetterWorldOptions.getMinecraft();
        WorldType worldType = ((BWOWorld) minecraft.world).bwo_getWorldType();
        boolean oldFeatures = ((BWOProperties) minecraft.world.getProperties()).bwo_isOldFeatures();
        Theme theme = ((BWOWorld) minecraft.world).bwo_getTheme();
        OldFeaturesProperties oldFeaturesProperties = worldType.getOldFeaturesProperties();

        if (minecraft.world.dimension.id == 0) {
            if (Config.BWOConfig.environment.oldTexturesAndSky && oldFeatures && oldFeaturesProperties != null && oldFeaturesProperties.oldBiome == null && oldFeaturesProperties.defaultFogColor != null && (theme.getFogColor() == -1)) {
                return oldFeaturesProperties.defaultFogColor.getColor(null, 0, 0);
            } else if (theme.getFogColor() != -1) {
                return theme.getFogColor();
            }
        }

        return original;
    }
}