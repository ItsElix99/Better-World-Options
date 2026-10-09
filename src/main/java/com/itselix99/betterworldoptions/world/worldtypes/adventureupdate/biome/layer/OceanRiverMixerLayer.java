package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer;

import com.itselix99.betterworldoptions.world.biome.IntCache;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.AdvUpdBiomeProperties;

public class OceanRiverMixerLayer extends Layer {
    private final Layer biomeLayer;
    private final Layer riverLayer;

    public OceanRiverMixerLayer(long seed, Layer biomeLayer, Layer riverLayer) {
        super(seed);
        this.biomeLayer = biomeLayer;
        this.riverLayer = riverLayer;
    }

    public void setLocalWorldSeed(long worldSeed) {
        this.biomeLayer.setLocalWorldSeed(worldSeed);
        this.riverLayer.setLocalWorldSeed(worldSeed);
        super.setLocalWorldSeed(worldSeed);
    }

    public int[] getArea(int x, int z, int sizeX, int sizeZ) {
        int[] var5 = this.biomeLayer.getArea(x, z, sizeX, sizeZ);
        int[] var6 = this.riverLayer.getArea(x, z, sizeX, sizeZ);
        int[] var7 = IntCache.get(sizeX * sizeZ);

        for(int var8 = 0; var8 < sizeX * sizeZ; ++var8) {
            if (var5[var8] == advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.OCEAN)) {
                var7[var8] = var5[var8];
            } else {
                var7[var8] = var6[var8] >= 0 ? var6[var8] : var5[var8];
            }
        }

        return var7;
    }
}