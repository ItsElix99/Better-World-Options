package com.itselix99.betterworldoptions.mixin.biomes;

import com.itselix99.betterworldoptions.BetterWorldOptions;
import com.itselix99.betterworldoptions.interfaces.BWOCustomRandomTreeFeature;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LargeOakTreeFeature;
import net.minecraft.world.gen.feature.OakTreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Random;

@Mixin(Biome.class)
public class BiomeMixin implements BWOCustomRandomTreeFeature {

    @Environment(EnvType.CLIENT)
    @ModifyReturnValue(method = "getSkyColor", at = @At("RETURN"))
    public int getSkyColor(int original) {
        if (this.equals(BetterWorldOptions.EarlyInfdev)) {
            return 200;
        } else if (this.equals(BetterWorldOptions.Infdev) || this.equals(BetterWorldOptions.IndevNormal)) {
            return 10079487;
        } else if (this.equals(BetterWorldOptions.IndevHell)) {
            return 1049600;
        } else if (this.equals(BetterWorldOptions.IndevParadise)) {
            return 13033215;
        } else if (this.equals(BetterWorldOptions.IndevWoods)) {
            return 7699847;
        } else if (this.equals(BetterWorldOptions.Alpha)) {
            return 8961023;
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
}