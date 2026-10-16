package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer.release11;

import com.itselix99.betterworldoptions.world.biome.IntCache;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer.Layer;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.AdvUpdBiomeProperties;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.Release11BiomeProperties;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.ReleaseBiomeProperties;
import net.minecraft.world.biome.Biome;

public class Release11ShoreLayer extends Layer {
    public Release11ShoreLayer(long seed, Layer parent) {
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
                int var10;
                int var11;
                int var12;
                int var13;
                if(var9 == advUpdBiomeProperties.getBiomeId(ReleaseBiomeProperties.MUSHROOM_ISLAND)) {
                    var10 = var5[var8 + 1 + (var7 + 1 - 1) * (sizeX + 2)];
                    var11 = var5[var8 + 1 + 1 + (var7 + 1) * (sizeX + 2)];
                    var12 = var5[var8 + 1 - 1 + (var7 + 1) * (sizeX + 2)];
                    var13 = var5[var8 + 1 + (var7 + 1 + 1) * (sizeX + 2)];
                    if(var10 != advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.OCEAN) && var11 != advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.OCEAN) && var12 != advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.OCEAN) && var13 != advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.OCEAN)) {
                        var6[var8 + var7 * sizeX] = var9;
                    } else {
                        var6[var8 + var7 * sizeX] = advUpdBiomeProperties.getBiomeId(ReleaseBiomeProperties.MUSHROOM_ISLAND_SHORE);
                    }
                } else if(var9 != advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.OCEAN) && var9 != advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.RIVER) && var9 != advUpdBiomeProperties.getBiomeId(Biome.SWAMPLAND) && var9 != advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.EXTREME_HILLS)) {
                    var10 = var5[var8 + 1 + (var7 + 1 - 1) * (sizeX + 2)];
                    var11 = var5[var8 + 1 + 1 + (var7 + 1) * (sizeX + 2)];
                    var12 = var5[var8 + 1 - 1 + (var7 + 1) * (sizeX + 2)];
                    var13 = var5[var8 + 1 + (var7 + 1 + 1) * (sizeX + 2)];
                    if(var10 != advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.OCEAN) && var11 != advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.OCEAN) && var12 != advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.OCEAN) && var13 != advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.OCEAN)) {
                        var6[var8 + var7 * sizeX] = var9;
                    } else {
                        var6[var8 + var7 * sizeX] = advUpdBiomeProperties.getBiomeId(Release11BiomeProperties.BEACH);
                    }
                } else if(var9 == advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.EXTREME_HILLS)) {
                    var10 = var5[var8 + 1 + (var7 + 1 - 1) * (sizeX + 2)];
                    var11 = var5[var8 + 1 + 1 + (var7 + 1) * (sizeX + 2)];
                    var12 = var5[var8 + 1 - 1 + (var7 + 1) * (sizeX + 2)];
                    var13 = var5[var8 + 1 + (var7 + 1 + 1) * (sizeX + 2)];
                    if(var10 == advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.EXTREME_HILLS) && var11 == advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.EXTREME_HILLS) && var12 == advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.EXTREME_HILLS) && var13 == advUpdBiomeProperties.getBiomeId(AdvUpdBiomeProperties.EXTREME_HILLS)) {
                        var6[var8 + var7 * sizeX] = var9;
                    } else {
                        var6[var8 + var7 * sizeX] = advUpdBiomeProperties.getBiomeId(Release11BiomeProperties.EXTREME_HILLS_EDGE);
                    }
                } else {
                    var6[var8 + var7 * sizeX] = var9;
                }
            }
        }

        return var6;
    }
}