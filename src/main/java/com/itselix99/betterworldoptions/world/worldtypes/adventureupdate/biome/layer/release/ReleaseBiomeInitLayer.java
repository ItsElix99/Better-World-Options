package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer.release;

import com.itselix99.betterworldoptions.world.biome.IntCache;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer.Layer;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.AdvUpdBiomeProperties;
import com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.properties.ReleaseBiomeProperties;
import net.minecraft.world.biome.Biome;

public class ReleaseBiomeInitLayer extends Layer {
    private final Biome[] biomes;

    public ReleaseBiomeInitLayer(long seed, Layer parent) {
        super(seed);
        this.biomes = new Biome[]{Biome.DESERT, Biome.FOREST, AdvUpdBiomeProperties.EXTREME_HILLS, Biome.SWAMPLAND, Biome.PLAINS, Biome.TAIGA};
        this.parent = parent;
    }

    public int[] getArea(int x, int z, int sizeX, int sizeZ) {
        int[] var5 = this.parent.getArea(x, z, sizeX, sizeZ);
        int[] var6 = IntCache.get(sizeX * sizeZ);

        for(int var7 = 0; var7 < sizeZ; ++var7) {
            for(int var8 = 0; var8 < sizeX; ++var8) {
                this.setChunkSeed(var8 + x, var7 + z);
                int var9 = var5[var8 + var7 * sizeX];
                if(var9 == 0) {
                    var6[var8 + var7 * sizeX] = 0;
                } else if(var9 == advUpdBiomeProperties.getBiomeId(ReleaseBiomeProperties.MUSHROOM_ISLAND)) {
                    var6[var8 + var7 * sizeX] = var9;
                } else if(var9 == 1) {
                    var6[var8 + var7 * sizeX] = advUpdBiomeProperties.getBiomeId(this.biomes[this.nextInt(this.biomes.length)]);
                } else {
                    var6[var8 + var7 * sizeX] = advUpdBiomeProperties.getBiomeId(ReleaseBiomeProperties.ICE_PLAINS);
                }
            }
        }

        return var6;
    }
}