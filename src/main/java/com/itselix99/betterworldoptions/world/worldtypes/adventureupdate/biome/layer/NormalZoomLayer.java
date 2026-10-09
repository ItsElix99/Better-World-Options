package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.biome.layer;

import com.itselix99.betterworldoptions.world.biome.IntCache;

public class NormalZoomLayer extends Layer {
    public NormalZoomLayer(long seed, Layer parent) {
        super(seed);
        super.parent = parent;
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
                var10[var14++ + var11] = this.pick(var15, var16);
                var10[var14] = this.pick(var15, var18);
                var10[var14++ + var11] = this.getModeOrRandom(var15, var18, var16, var19);
                var15 = var18;
                var16 = var19;
            }
        }

        int[] var20 = IntCache.get(sizeX * sizeZ);

        for(int var21 = 0; var21 < sizeZ; ++var21) {
            System.arraycopy(var10, (var21 + (z & 1)) * (var7 << 1) + (x & 1), var20, var21 * sizeX, sizeX);
        }

        return var20;
    }

    protected int pick(int i1, int i2) {
        return this.nextInt(2) == 0 ? i1 : i2;
    }

    protected int getModeOrRandom(int i1, int i2, int i3, int i4) {
        if (i2 == i3 && i3 == i4) {
            return i2;
        } else if (i1 == i2 && i1 == i3) {
            return i1;
        } else if (i1 == i2 && i1 == i4) {
            return i1;
        } else if (i1 == i3 && i1 == i4) {
            return i1;
        } else if (i1 == i2 && i3 != i4) {
            return i1;
        } else if (i1 == i3 && i2 != i4) {
            return i1;
        } else if (i1 == i4 && i2 != i3) {
            return i1;
        } else if (i2 == i1 && i3 != i4) {
            return i2;
        } else if (i2 == i3 && i1 != i4) {
            return i2;
        } else if (i2 == i4 && i1 != i3) {
            return i2;
        } else if (i3 == i1 && i2 != i4) {
            return i3;
        } else if (i3 == i2 && i1 != i4) {
            return i3;
        } else if (i3 == i4 && i1 != i2) {
            return i3;
        } else if (i4 == i1 && i2 != i3) {
            return i3;
        } else if (i4 == i2 && i1 != i3) {
            return i3;
        } else if (i4 == i3 && i1 != i2) {
            return i3;
        } else {
            int var5 = this.nextInt(4);
            if (var5 == 0) {
                return i1;
            } else if (var5 == 1) {
                return i2;
            } else {
                return var5 == 2 ? i3 : i4;
            }
        }
    }

    public static Layer zoom(long seed, Layer layer, int magnification) {
        Layer var4 = layer;

        for(int var5 = 0; var5 < magnification; ++var5) {
            var4 = new NormalZoomLayer(seed + (long)var5, var4);
        }

        return var4;
    }
}