package com.itselix99.betterworldoptions.mixin.biomes;

import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import net.minecraft.world.biome.RainforestBiome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LargeOakTreeFeature;
import net.minecraft.world.gen.feature.OakTreeFeature;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(RainforestBiome.class)
public abstract class RainforestBiomeMixin extends BiomeMixin implements BWOWorld {

    @Override
    public Feature bwo_getRandomTreeFeatureInfdev611(Random random) {
        return new OakTreeFeature();
    }

    @Override
    public Feature bwo_getRandomTreeFeatureInfdev(Random random) {
        return new LargeOakTreeFeature();
    }

    @Override
    public Feature bwo_getRandomTreeFeatureEarlyInfdev(Random random) {
        return new OakTreeFeature();
    }
}