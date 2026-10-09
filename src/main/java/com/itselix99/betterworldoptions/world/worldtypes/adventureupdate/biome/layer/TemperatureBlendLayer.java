package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer;

import com.itselix99.betterworldoptions.world.biome.IntCache;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.AdvUpdBiomeProperties;

public class TemperatureBlendLayer extends Layer {
    private final Layer temperatures;
    private final int step;

    public TemperatureBlendLayer(Layer temperatures, Layer parent, int step) {
        super(0L);
        this.parent = parent;
        this.temperatures = temperatures;
        this.step = step;
    }

    public int[] getArea(int x, int z, int sizeX, int sizeZ) {
        int[] var5 = this.parent.getArea(x, z, sizeX, sizeZ);
        int[] var6 = this.temperatures.getArea(x, z, sizeX, sizeZ);
        int[] var7 = IntCache.get(sizeX * sizeZ);

        for(int var8 = 0; var8 < sizeX * sizeZ; ++var8) {
            var7[var8] = var6[var8] + (advUpdBiomeProperties.getScaledTemperature(AdvUpdBiomeProperties.BY_ID[var5[var8]]) - var6[var8]) / (this.step * 2 + 1);
        }

        return var7;
    }
}