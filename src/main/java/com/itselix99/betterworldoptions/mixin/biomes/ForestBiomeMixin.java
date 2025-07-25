package com.itselix99.betterworldoptions.mixin.biomes;

import com.itselix99.betterworldoptions.interfaces.BWOCustomRandomTreeFeature;
import net.minecraft.world.biome.ForestBiome;
import net.minecraft.world.gen.feature.BirchTreeFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LargeOakTreeFeature;
import net.minecraft.world.gen.feature.OakTreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import java.util.Random;

@Mixin(ForestBiome.class)
public class ForestBiomeMixin extends BiomeMixin implements BWOCustomRandomTreeFeature {

    public Feature bwo_getRandomTreeFeatureInfdev611(Random random) {
        if (random.nextInt(5) == 0) {
            return new BirchTreeFeature();
        } else {
            return new OakTreeFeature();
        }
    }

    @Override
    public Feature bwo_getRandomTreeFeatureInfdev(Random random) {
        if (random.nextInt(5) == 0) {
            return new BirchTreeFeature();
        } else {
            return new LargeOakTreeFeature();
        }
    }

    @Override
    public Feature bwo_getRandomTreeFeatureEarlyInfdev(Random random) {
        if (random.nextInt(5) == 0) {
            return new BirchTreeFeature();
        } else {
            return new OakTreeFeature();
        }
    }
}