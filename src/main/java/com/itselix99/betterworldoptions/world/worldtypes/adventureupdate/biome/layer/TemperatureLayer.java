package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer;

import com.itselix99.betterworldoptions.world.biome.IntCache;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.AdvUpdBiomeProperties;

public class TemperatureLayer extends Layer {
    public TemperatureLayer(Layer parent) {
        super(0L);
        this.parent = parent;
    }

    public int[] getArea(int x, int z, int sizeX, int sizeZ) {
        int[] var5 = this.parent.getArea(x, z, sizeX, sizeZ);
        int[] var6 = IntCache.get(sizeX * sizeZ);

        for(int var7 = 0; var7 < sizeX * sizeZ; ++var7) {
            var6[var7] = advUpdBiomeProperties.getScaledTemperature(AdvUpdBiomeProperties.BY_ID[var5[var7]]);
        }

        return var6;
    }
}