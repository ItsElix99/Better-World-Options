package com.itselix99.betterworldoptions.mixin.biomes;

import com.itselix99.betterworldoptions.interfaces.BWOWorld;
import net.minecraft.world.biome.ForestBiome;
import net.minecraft.world.gen.feature.BirchTreeFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LargeOakTreeFeature;
import net.minecraft.world.gen.feature.OakTreeFeature;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(ForestBiome.class)
public abstract class ForestBiomeMixin extends BiomeMixin implements BWOWorld {

    @Override
    public Feature bwo_getRandomTreeFeatureInfdev(Random random) {
        if (random.nextInt(5) == 0) {
            return new BirchTreeFeature();
        } else {
            return random.nextInt(3) == 0 ? new OakTreeFeature() : new LargeOakTreeFeature();
        }
    }

    @Override
    public Feature bwo_getRandomTreeFeatureMCPE(Random random) {
        if (random.nextInt(5) == 0) {
            return new BirchTreeFeature();
        } else {
            return super.bwo_getRandomTreeFeatureMCPE(random);
        }
    }
}