package com.itselix99.betterworldoptions.mixin.biomes;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LargeOakTreeFeature;
import net.minecraft.world.gen.feature.OakTreeFeature;
import net.modificationstation.stationapi.api.worldgen.biome.StationBiome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Random;

@Mixin(Biome.class)
public abstract class BiomeMixin implements BWOWorld, StationBiome {

    @Environment(EnvType.CLIENT)
    @ModifyReturnValue(method = "getSkyColor", at = @At("RETURN"))
    public int getSkyColor(int original) {
        Minecraft minecraft = (Minecraft) FabricLoaderImpl.INSTANCE.getGameInstance();
        String worldType = ((BWOProperties) minecraft.world.getProperties()).bwo_getWorldType();
        boolean oldFeatures = ((BWOProperties) minecraft.world.getProperties()).bwo_isOldFeatures();
        String theme = ((BWOProperties) minecraft.world.getProperties()).bwo_getTheme();

        if (theme.equals("Hell")) {
            return 1049600;
        } else if (theme.equals("Paradise")) {
            return 13033215;
        } else if (theme.equals("Woods")) {
            return 7699847;
        } else if (Biome.class.cast(this) == BetterWorldOptions.Alpha) {
            return 8961023;
        } else if (Biome.class.cast(this) == BetterWorldOptions.Infdev) {
            return 10079487;
        } else if (Biome.class.cast(this) == BetterWorldOptions.EarlyInfdev) {
            return 200;
        } else if (Biome.class.cast(this) == BetterWorldOptions.Indev) {
            return 10079487;
        } else if (worldType.equals("MCPE") && oldFeatures) {
            return 2907587;
        } else {
            return original;
        }
    }

    @Override
    public Feature bwo_getRandomTreeFeatureInfdev611(Random random) { return new OakTreeFeature(); }

    @Override
    public Feature bwo_getRandomTreeFeatureInfdev(Random random) { return new LargeOakTreeFeature(); }

    @Override
    public Feature bwo_getRandomTreeFeatureEarlyInfdev(Random random) {
        return new OakTreeFeature();
    }

    @Override
    public Feature bwo_getRandomTreeFeatureMCPE(Random random) {
        random.nextInt();
        return new OakTreeFeature();
    }
}