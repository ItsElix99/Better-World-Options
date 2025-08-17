package com.itselix99.betterworldoptions.mixin.biomes;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import com.itselix99.betterworldoptions.interfaces.BWOProperties;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
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
        boolean betaFeatures = ((BWOProperties) minecraft.world.getProperties()).bwo_getBetaFeatures();
        String theme = ((BWOProperties) minecraft.world.getProperties()).bwo_getTheme();

        if (this.equals(BetterWorldOptions.EarlyInfdev)) {
            return 200;
        } else if (this.equals(BetterWorldOptions.Infdev) || this.equals(BetterWorldOptions.IndevNormal)) {
            return 10079487;
        } else if (this.equals(BetterWorldOptions.IndevHell) || theme.equals("Hell")) {
            return 1049600;
        } else if (this.equals(BetterWorldOptions.IndevParadise) || theme.equals("Paradise")) {
            return 13033215;
        } else if (this.equals(BetterWorldOptions.IndevWoods) || theme.equals("Woods")) {
            return 7699847;
        } else if (this.equals(BetterWorldOptions.Alpha)) {
            return 8961023;
        } else if (worldType.equals("MCPE") && !betaFeatures) {
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