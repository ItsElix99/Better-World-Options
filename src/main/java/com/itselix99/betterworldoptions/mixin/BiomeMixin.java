package com.itselix99.betterworldoptions.mixin;

import com.itselix99.betterworldoptions.interfaces.CustomRandomTreeFeature;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LargeOakTreeFeature;
import net.minecraft.world.gen.feature.OakTreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import java.util.Random;

@Mixin(Biome.class)
public class BiomeMixin implements CustomRandomTreeFeature {

    @Override
    public Feature bwo_getRandomTreeFeatureInfdev(Random random) { return new LargeOakTreeFeature(); }

    @Override
    public Feature bwo_getRandomTreeFeatureEarlyInfdev(Random random) {
        return new OakTreeFeature();
    }
}