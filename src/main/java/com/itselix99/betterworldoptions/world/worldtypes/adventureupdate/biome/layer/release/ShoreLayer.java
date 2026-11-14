package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer.release;

import com.itselix99.betterworldoptions.interfaces.BWOBiome;
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
                if(var9 == ((BWOBiome) ReleaseBiomeProperties.MUSHROOM_ISLAND).bwo_getBiomeId()) {
                    int var10 = var5[var8 + 1 + (var7 + 1 - 1) * (sizeX + 2)];
                    int var11 = var5[var8 + 1 + 1 + (var7 + 1) * (sizeX + 2)];
                    int var12 = var5[var8 + 1 - 1 + (var7 + 1) * (sizeX + 2)];
                    int var13 = var5[var8 + 1 + (var7 + 1 + 1) * (sizeX + 2)];
                    if(var10 != ((BWOBiome) AdvUpdBiomeProperties.OCEAN).bwo_getBiomeId() && var11 != ((BWOBiome) AdvUpdBiomeProperties.OCEAN).bwo_getBiomeId() && var12 != ((BWOBiome) AdvUpdBiomeProperties.OCEAN).bwo_getBiomeId() && var13 != ((BWOBiome) AdvUpdBiomeProperties.OCEAN).bwo_getBiomeId()) {
                        var6[var8 + var7 * sizeX] = var9;
                    } else {
                        var6[var8 + var7 * sizeX] = ((BWOBiome) ReleaseBiomeProperties.MUSHROOM_ISLAND_SHORE).bwo_getBiomeId();
                    }
                } else {
                    var6[var8 + var7 * sizeX] = var9;
                }
            }
        }

        return var6;
    }
}