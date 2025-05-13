package com.itselix99.betterworldoptions.mixin;

import com.itselix99.betterworldoptions.interfaces.CustomRandomTreeFeature;
import net.minecraft.world.biome.TaigaBiome;
import net.minecraft.world.gen.feature.*;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(TaigaBiome.class)
public class TaigaBiomeMixin extends BiomeMixin implements CustomRandomTreeFeature {

    @Override
    public Feature bwo_getRandomTreeFeatureInfdev(Random random) {
        return random.nextInt(3) == 0 ? new PineTreeFeature() : new SpruceTreeFeature();
    }

    @Override
    public Feature bwo_getRandomTreeFeatureEarlyInfdev(Random random) {
        return random.nextInt(3) == 0 ? new PineTreeFeature() : new SpruceTreeFeature();
    }
}