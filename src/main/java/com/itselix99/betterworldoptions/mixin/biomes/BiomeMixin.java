package com.itselix99.betterworldoptions.mixin.biomes;

import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.worldtype.OldFeaturesProperties;
import com.itselix99.betterworldoptions.api.worldtype.WorldTypes;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LargeOakTreeFeature;
import net.minecraft.world.gen.feature.OakTreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Random;

@Mixin(Biome.class)
public abstract class BiomeMixin implements BWOWorld {

    @Environment(EnvType.CLIENT)
    @ModifyReturnValue(method = "getSkyColor", at = @At("RETURN"))
    public int bwo_getSkyColor(int original) {
        Minecraft minecraft = (Minecraft) FabricLoaderImpl.INSTANCE.getGameInstance();
        String worldType = ((BWOProperties) minecraft.world.getProperties()).bwo_getWorldType();
        boolean oldFeatures = ((BWOProperties) minecraft.world.getProperties()).bwo_isOldFeatures();
        String theme = ((BWOProperties) minecraft.world.getProperties()).bwo_getTheme();
        OldFeaturesProperties oldFeaturesProperties = WorldTypes.getOldFeaturesProperties(worldType);

        if (theme.equals("Hell")) {
            return 1049600;
        } else if (theme.equals("Paradise")) {
            return 13033215;
        } else if (theme.equals("Woods")) {
            return 7699847;
        } else if (Config.BWOConfig.environment.oldTexturesAndSky && oldFeatures && oldFeaturesProperties != null && (oldFeaturesProperties.oldFeaturesBiomeSupplier.get() != null || oldFeaturesProperties.defaultSkyColor != -1)) {
            return oldFeaturesProperties.defaultSkyColor;
        }

        return original;
    }

    @ModifyReturnValue(method = "getRandomTreeFeature", at = @At("RETURN"))
    public Feature bwo_getRandomTreeFeatureInfdev(Feature original, @Local(ordinal = 0, argsOnly = true) Random random) {
        BWOWorldPropertiesStorage bwoWorldPropertiesStorage = BWOWorldPropertiesStorage.getInstance();
        String worldType = bwoWorldPropertiesStorage.getStringOptionValue("WorldType", OptionType.GENERAL_OPTION);

        if (worldType.equals("Infdev 415") || worldType.equals("Infdev 420")) {
            return random.nextInt(10) == 0 ? new LargeOakTreeFeature() : new OakTreeFeature();
        } else {
            return original;
        }
    }

    @Override
    public Feature bwo_getRandomTreeFeatureMCPE(Random random) {
        random.nextInt();
        return new OakTreeFeature();
    }
}