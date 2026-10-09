package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer;

import com.itselix99.betterworldoptions.world.biome.IntCache;

public class IslandLayer extends Layer {
    public IslandLayer(long seed) {
        super(seed);
    }

    public int[] getArea(int x, int z, int sizeX, int sizeZ) {
        int[] var5 = IntCache.get(sizeX * sizeZ);

        for(int var6 = 0; var6 < sizeZ; ++var6) {
            for(int var7 = 0; var7 < sizeX; ++var7) {
                this.setChunkSeed(x + var7, z + var6);
                var5[var7 + var6 * sizeX] = this.nextInt(10) == 0 ? 1 : 0;
            }
        }

        if (x > -sizeX && x <= 0 && z > -sizeZ && z <= 0) {
            var5[-x + -z * sizeX] = 1;
        }

        return var5;
    }
}