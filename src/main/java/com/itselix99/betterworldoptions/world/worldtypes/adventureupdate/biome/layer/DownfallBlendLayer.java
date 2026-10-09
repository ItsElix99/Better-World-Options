package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer;

import com.itselix99.betterworldoptions.world.biome.IntCache;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.AdvUpdBiomeProperties;

public class DownfallBlendLayer extends Layer {
    private final Layer downfall;
    private final int step;

    public DownfallBlendLayer(Layer downfall, Layer parent, int step) {
        super(0L);
        this.parent = parent;
        this.downfall = downfall;
        this.step = step;
    }

    public int[] getArea(int x, int z, int sizeX, int sizeZ) {
        int[] var5 = this.parent.getArea(x, z, sizeX, sizeZ);
        int[] var6 = this.downfall.getArea(x, z, sizeX, sizeZ);
        int[] var7 = IntCache.get(sizeX * sizeZ);

        for(int var8 = 0; var8 < sizeX * sizeZ; ++var8) {
            var7[var8] = var6[var8] + (advUpdBiomeProperties.getScaledDownfall(AdvUpdBiomeProperties.BY_ID[var5[var8]]) - var6[var8]) / (this.step + 1);
        }

        return var7;
    }
}