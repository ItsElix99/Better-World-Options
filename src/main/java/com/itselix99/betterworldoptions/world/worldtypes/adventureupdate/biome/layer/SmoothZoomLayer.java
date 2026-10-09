package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer;

import com.itselix99.betterworldoptions.world.biome.IntCache;

public class SmoothZoomLayer extends Layer {
    public SmoothZoomLayer(long seed, Layer parent) {
        super(seed);
        this.parent = parent;
    }

    public int[] getArea(int x, int z, int sizeX, int sizeZ) {
        int var5 = x >> 1;
        int var6 = z >> 1;
        int var7 = (sizeX >> 1) + 3;
        int var8 = (sizeZ >> 1) + 3;
        int[] var9 = this.parent.getArea(var5, var6, var7, var8);
        int[] var10 = IntCache.get(var7 * 2 * var8 * 2);
        int var11 = var7 << 1;

        for(int var12 = 0; var12 < var8 - 1; ++var12) {
            int var13 = var12 << 1;
            int var14 = var13 * var11;
            int var15 = var9[(var12) * var7];
            int var16 = var9[(var12 + 1) * var7];

            for(int var17 = 0; var17 < var7 - 1; ++var17) {
                this.setChunkSeed((long)(var17 + var5 << 1), (long)(var12 + var6 << 1));
                int var18 = var9[var17 + 1 + (var12) * var7];
                int var19 = var9[var17 + 1 + (var12 + 1) * var7];
                var10[var14] = var15;
                var10[var14++ + var11] = var15 + (var16 - var15) * this.nextInt(256) / 256;
                var10[var14] = var15 + (var18 - var15) * this.nextInt(256) / 256;
                int var20 = var15 + (var18 - var15) * this.nextInt(256) / 256;
                int var21 = var16 + (var19 - var16) * this.nextInt(256) / 256;
                var10[var14++ + var11] = var20 + (var21 - var20) * this.nextInt(256) / 256;
                var15 = var18;
                var16 = var19;
            }
        }

        int[] var22 = IntCache.get(sizeX * sizeZ);

        for(int var23 = 0; var23 < sizeZ; ++var23) {
            System.arraycopy(var10, (var23 + (z & 1)) * (var7 << 1) + (x & 1), var22, var23 * sizeX, sizeX);
        }

        return var22;
    }

    public static Layer zoom(long seed, Layer layer, int magnification) {
        Layer var4 = layer;

        for(int var5 = 0; var5 < magnification; ++var5) {
            var4 = new SmoothZoomLayer(seed + (long)var5, var4);
        }

        return var4;
    }
}