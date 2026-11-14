package com.itselix99.betterworldoptions.mixin.biomes;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.api.options.OptionType;
import com.itselix99.betterworldoptions.api.theme.Theme;
import com.itselix99.betterworldoptions.api.worldtype.OldFeaturesProperties;
import com.itselix99.betterworldoptions.api.worldtype.WorldType;
import com.itselix99.betterworldoptions.config.Config;
import com.itselix99.betterworldoptions.interfaces.BWOBiome;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.world.BWOWorldPropertiesStorage;
import com.itselix99.betterworldoptions.world.feature.FeatureDecorator;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LargeOakTreeFeature;
import net.minecraft.world.gen.feature.OakTreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Random;

@Mixin(Biome.class)
public class BiomeMixin implements BWOBiome {
    @Unique protected int biomeId;
    @Unique protected float baseHeight;
    @Unique protected float heightVariation;
    @Unique protected float temperature;
    @Unique protected float downfall;
    @Unique protected FeatureDecorator featureDecorator;

    @Environment(EnvType.CLIENT)
    @ModifyReturnValue(method = "getSkyColor", at = @At("RETURN"))
    public int bwo_getSkyColor(int original) {
        Minecraft minecraft = BetterWorldOptions.getMinecraft();
        WorldType worldType = ((BWOWorld) minecraft.world).bwo_getWorldType();
        boolean oldFeatures = ((BWOProperties) minecraft.world.getProperties()).bwo_isOldFeatures();
        Theme theme = ((BWOWorld) minecraft.world).bwo_getTheme();
        OldFeaturesProperties oldFeaturesProperties = worldType.getOldFeaturesProperties();

        if (theme.getSkyColor() != -1) {
            return theme.getSkyColor();
        } else if (Config.BWOConfig.environment.oldTexturesAndSky && oldFeatures && oldFeaturesProperties != null && (oldFeaturesProperties.oldBiome != null || oldFeaturesProperties.defaultSkyColor != -1)) {
            return oldFeaturesProperties.defaultSkyColor;
        }

        return original;
    }

    @ModifyReturnValue(method = "getRandomTreeFeature", at = @At("RETURN"))
    public Feature bwo_getRandomTreeFeatureInfdev(Feature original, @Local(ordinal = 0, argsOnly = true) Random random) {
        BWOWorldPropertiesStorage bwoWorldPropertiesStorage = BWOWorldPropertiesStorage.getInstance();
        String worldType = bwoWorldPropertiesStorage.getOptionValue("WorldType", OptionType.GENERAL_OPTION, "");

        if (worldType.equals(BetterWorldOptions.NAMESPACE.id("infdev_20100415").toString()) || worldType.equals(BetterWorldOptions.NAMESPACE.id("infdev_20100420").toString())) {
            return random.nextInt(10) == 0 ? new LargeOakTreeFeature() : new OakTreeFeature();
        } else {
            return original;
        }
    }

    public void bwo_setBiomeId(int biomeId) {
        this.biomeId = biomeId;
    }

    public void bwo_setHeight(float baseHeight, float heightVariation) {
        this.baseHeight = baseHeight;
        this.heightVariation = heightVariation;
    }

    public void bwo_setTemperatureAndDownfall(float temperature, float downfall) {
        this.temperature = temperature;
        this.downfall = downfall;
    }

    public void bwo_setFeatureDecorator(FeatureDecorator featureDecorator) {
        this.featureDecorator = featureDecorator;
    }

    public int bwo_getBiomeId() {
        return this.biomeId;
    }

    public float bwo_getBaseHeight() {
        return this.baseHeight;
    }

    public float bwo_getHeightVariation() {
        return this.heightVariation;
    }

    public float bwo_getTemperature() {
        return this.temperature;
    }

    public float bwo_getDownfall() {
        return this.downfall;
    }

    public FeatureDecorator bwo_getFeatureDecorator() {
        return this.featureDecorator;
    }

    public void bwo_decorate(World world, Random random, int x, int z) {
        this.featureDecorator.decorate(world, random, x, z);
    }
}