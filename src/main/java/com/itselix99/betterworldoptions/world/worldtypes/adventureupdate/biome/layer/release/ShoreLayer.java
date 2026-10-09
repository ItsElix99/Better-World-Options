package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer.release;

import com.itselix99.betterworldoptions.world.biome.IntCache;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer.Layer;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.AdvUpdBiomeProperties;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.ReleaseBiomeProperties;

public class ShoreLayer extends Layer {
    public ShoreLayer(long seed, Layer parent) {
        super(seed);
        this.parent = parent;
    }

    public int[] getArea(int x, int z, int sizeX, int sizeZ) {
        int[] var5 = this.parent.getArea(x - 1, z - 1, sizeX + 2, sizeZ + 2);
        int[] var6 = IntCache.get(sizeX * sizeZ);

        for(int var7 = 0; var7 < sizeZ; ++var7) {
            for(int var8 = 0; var8 < sizeX; ++var8) {
                this.setChunkSeed(var8 + x, var7 + z);
                int var9 = var5[var8 + 1 + (var7 + 1) * (sizeX + 2)];
                if(var9 == advUpdBiomeProperties.getBiomeId(ReleaseBiomeProperties.MUSHROOM_ISLAND)) {
                    int var10 = var5[var8 + 1 + (var7 + 1 - 1) * (sizeX + 2)];
                    int var11 = var5[var8 + 1 + 1 + (var7 + 1) * (sizeX + 2)];
                    int var12 = var5[var8 + 1 - 1 + (var7 + 1) * (sizeX + 2)];
                    int var13 = var5[var8 + 1 + (var7 + 1 + 1) * (sizeX + 2)];
                    if(var10 != advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.OCEAN) && var11 != advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.OCEAN) && var12 != advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.OCEAN) && var13 != advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.OCEAN)) {
                        var6[var8 + var7 * sizeX] = var9;
                    } else {
                        var6[var8 + var7 * sizeX] = advUpdBiomeProperties.getBiomeId(ReleaseBiomeProperties.MUSHROOM_ISLAND_SHORE);
                    }
                } else {
                    var6[var8 + var7 * sizeX] = var9;
                }
            }
        }

        return var6;
    }
}