package com.itselix99.betterworldoptions.interfaces;

import com.itselix99.betterworldoptions.world.feature.FeatureDecorator;
import net.minecraft.world.World;

import java.util.Random;

public interface BWOBiome {
    void bwo_setBiomeId(int biomeId);
    void bwo_setHeight(float baseHeight, float heightVariation);
    void bwo_setTemperatureAndDownfall(float temperature, float downfall);
    void bwo_setFeatureDecorator(FeatureDecorator featureDecorator);

    int bwo_getBiomeId();
    float bwo_getBaseHeight();
    float bwo_getHeightVariation();
    float bwo_getTemperature();
    float bwo_getDownfall();
    FeatureDecorator bwo_getFeatureDecorator();

    default int bwo_getScaledDownfall() {
        return (int)(this.bwo_getDownfall() * 65536.0F);
    }

    default int bwo_getScaledTemperature() {
        return (int)(this.bwo_getTemperature() * 65536.0F);
    }

    void bwo_decorate(World world, Random random, int x, int z);
}