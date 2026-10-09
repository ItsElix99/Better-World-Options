package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer.release;

import com.itselix99.betterworldoptions.world.biome.IntCache;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer.Layer;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.ReleaseBiomeProperties;

public class AddSnowyBiomesLayer extends Layer {
    public AddSnowyBiomesLayer(long seed, Layer parent) {
        super(seed);
        this.parent = parent;
    }

    public int[] getArea(int x, int z, int sizeX, int sizeZ) {
        int var5 = x - 1;
        int var6 = z - 1;
        int var7 = sizeX + 2;
        int var8 = sizeZ + 2;
        int[] var9 = this.parent.getArea(var5, var6, var7, var8);
        int[] var10 = IntCache.get(sizeX * sizeZ);

        for (int var11 = 0; var11 < sizeZ; ++var11) {
            for (int var12 = 0; var12 < sizeX; ++var12) {
                int var13 = var9[var12 + 1 + (var11 + 1) * var7];
                this.setChunkSeed(var12 + x, var11 + z);
                if (var13 == 0) {
                    var10[var12 + var11 * sizeX] = 0;
                } else {
                    int var14 = this.nextInt(5);
                    if (var14 == 0) {
                        var14 = advUpdBiomeProperties.getBiomeId(ReleaseBiomeProperties.ICE_PLAINS);
                    } else {
                        var14 = 1;
                    }

                    var10[var12 + var11 * sizeX] = var14;
                }
            }
        }

        return var10;
    }
}