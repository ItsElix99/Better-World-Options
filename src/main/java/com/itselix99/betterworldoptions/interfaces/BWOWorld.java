package com.itselix99.betterworldoptions.interfaces;

import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public interface BWOWorld {
    Feature bwo_getRandomTreeFeatureInfdev611(Random random);
    Feature bwo_getRandomTreeFeatureInfdev(Random random);
    Feature bwo_getRandomTreeFeatureEarlyInfdev(Random random);
    Feature bwo_getRandomTreeFeatureMCPE(Random random);

    void bwo_setSnow(boolean bl);
    void bwo_oldBiomeSetSnow(String worldtype, boolean bl);
    void bwo_setPrecipitation(boolean bl);
    void bwo_oldBiomeSetPrecipitation(String worldtype, boolean bl);
}