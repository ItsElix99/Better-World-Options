package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer;

import com.itselix99.betterworldoptions.world.biome.IntCache;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.AdvUpdBiomeProperties;

public class RiverLayer extends Layer {
    public RiverLayer(long seed, Layer parent) {
        super(seed);
        super.parent = parent;
    }

    public int[] getArea(int x, int z, int sizeX, int sizeZ) {
        int var5 = x - 1;
        int var6 = z - 1;
        int var7 = sizeX + 2;
        int var8 = sizeZ + 2;
        int[] var9 = this.parent.getArea(var5, var6, var7, var8);
        int[] var10 = IntCache.get(sizeX * sizeZ);

        for(int var11 = 0; var11 < sizeZ; ++var11) {
            for(int var12 = 0; var12 < sizeX; ++var12) {
                int var13 = var9[var12 + (var11 + 1) * var7];
                int var14 = var9[var12 + 2 + (var11 + 1) * var7];
                int var15 = var9[var12 + 1 + (var11) * var7];
                int var16 = var9[var12 + 1 + (var11 + 2) * var7];
                int var17 = var9[var12 + 1 + (var11 + 1) * var7];
                if (var17 != 0 && var13 != 0 && var14 != 0 && var15 != 0 && var16 != 0) {
                    if (var17 == var13 && var17 == var15 && var17 == var14 && var17 == var16) {
                        var10[var12 + var11 * sizeX] = -1;
                    } else {
                        var10[var12 + var11 * sizeX] = advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.RIVER);
                    }
                } else {
                    var10[var12 + var11 * sizeX] = advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.RIVER);
                }
            }
        }

        return var10;
    }
}