package com.itselix99.betterworldoptions.mixin.biomes;

import com.itselix99.betterworldoptions.interfaces.BWOBiome;
import net.minecraft.world.biome.TaigaBiome;
import net.minecraft.world.gen.feature.*;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(TaigaBiome.class)
public abstract class TaigaBiomeMixin extends BiomeMixin implements BWOBiome {

    @Override
    public Feature bwo_getRandomTreeFeatureInfdev611(Random random) {
        return random.nextInt(3) == 0 ? new PineTreeFeature() : new SpruceTreeFeature();
    }

    @Override
    public Feature bwo_getRandomTreeFeatureInfdev(Random random) {
        return random.nextInt(3) == 0 ? new PineTreeFeature() : new SpruceTreeFeature();
    }

    @Override
    public Feature bwo_getRandomTreeFeatureEarlyInfdev(Random random) {
        return random.nextInt(3) == 0 ? new PineTreeFeature() : new SpruceTreeFeature();
    }

    @Override
    public Feature bwo_getRandomTreeFeatureMCPE(Random random) {
        return random.nextInt(3) == 0 ? new PineTreeFeature() : new SpruceTreeFeature();
    }
}