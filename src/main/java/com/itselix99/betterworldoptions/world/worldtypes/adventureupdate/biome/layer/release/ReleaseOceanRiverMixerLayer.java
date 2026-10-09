package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer.release;

import com.itselix99.betterworldoptions.world.biome.IntCache;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer.Layer;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.AdvUpdBiomeProperties;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.ReleaseBiomeProperties;

public class ReleaseOceanRiverMixerLayer extends Layer {
    private final Layer biomeLayer;
    private final Layer riverLayer;

    public ReleaseOceanRiverMixerLayer(long seed, Layer biomeLayer, Layer riverLayer) {
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
            if(var5[var8] == advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.OCEAN)) {
                var7[var8] = var5[var8];
            } else if(var6[var8] >= 0) {
                if(var5[var8] == advUpdBiomeProperties.getBiomeId(ReleaseBiomeProperties.ICE_PLAINS)) {
                    var7[var8] = advUpdBiomeProperties.getBiomeId(ReleaseBiomeProperties.FROZEN_RIVER);
                } else if(var5[var8] != advUpdBiomeProperties.getBiomeId(ReleaseBiomeProperties.MUSHROOM_ISLAND) && var5[var8] != advUpdBiomeProperties.getBiomeId(ReleaseBiomeProperties.MUSHROOM_ISLAND_SHORE)) {
                    var7[var8] = var6[var8];
                } else {
                    var7[var8] = advUpdBiomeProperties.getBiomeId(ReleaseBiomeProperties.MUSHROOM_ISLAND_SHORE);
                }
            } else {
                var7[var8] = var5[var8];
            }
        }

        return var7;
    }
}