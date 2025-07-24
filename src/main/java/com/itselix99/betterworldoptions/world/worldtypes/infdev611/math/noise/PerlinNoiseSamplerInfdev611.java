package com.itselix99.betterworldoptions.world.worldtypes.infdev611.math.noise;

import net.minecraft.util.math.noise.NoiseSampler;

import java.util.Random;

public final class PerlinNoiseSamplerInfdev611 extends NoiseSampler {
    private final int[] perm;
    private final double offsetX;
    private final double offsetY;
    private final double offsetZ;

    public PerlinNoiseSamplerInfdev611() {
        this(new Random());
    }

    public PerlinNoiseSamplerInfdev611(Random random) {
        this.perm = new int[512];
        this.offsetX = random.nextDouble() * 256.0D;
        this.offsetY = random.nextDouble() * 256.0D;
        this.offsetZ = random.nextDouble() * 256.0D;

        for(int var2 = 0; var2 < 256; this.perm[var2] = var2++) {
        }

        for(int var5 = 0; var5 < 256; ++var5) {
            int var3 = random.nextInt(256 - var5) + var5;
            int var4 = this.perm[var5];
            this.perm[var5] = this.perm[var3];
            this.perm[var3] = var4;
            this.perm[var5 + 256] = this.perm[var5];
        }

    }

    private double sample(double x, double y, double z) {
        double var7 = x + this.offsetX;
        double var9 = y + this.offsetY;
        double var11 = z + this.offsetZ;
        int var25 = (int)var7;
        int var2 = (int)var9;
        int var29 = (int)var11;
        if (var7 < (double)var25) {
            --var25;
        }

        if (var9 < (double)var2) {
            --var2;
        }

        if (var11 < (double)var29) {
            --var29;
        }

        int var4 = var25 & 255;
        int var33 = var2 & 255;
        int var6 = var29 & 255;
        var7 -= var25;
        var9 -= var2;
        var11 -= var29;
        double g = var7 * var7 * var7 * (var7 * (var7 * 6.0D - 15.0D) + 10.0D);
        double h = var9 * var9 * var9 * (var9 * (var9 * 6.0D - 15.0D) + 10.0D);
        double i = var11 * var11 * var11 * (var11 * (var11 * 6.0D - 15.0D) + 10.0D);
        var25 = this.perm[var4] + var33;
        var2 = this.perm[var25] + var6;
        var25 = this.perm[var25 + 1] + var6;
        var29 = this.perm[var4 + 1] + var33;
        var4 = this.perm[var29] + var6;
        var29 = this.perm[var29 + 1] + var6;
        return lerp(i, lerp(h, lerp(g, gradCoord(this.perm[var2], var7, var9, var11), gradCoord(this.perm[var4], var7 - 1.0D, var9, var11)), lerp(g, gradCoord(this.perm[var25], var7, var9 - 1.0D, var11), gradCoord(this.perm[var29], var7 - 1.0D, var9 - 1.0D, var11))), lerp(h, lerp(g, gradCoord(this.perm[var2 + 1], var7, var9, var11 - 1.0D), gradCoord(this.perm[var4 + 1], var7 - 1.0D, var9, var11 - 1.0D)), lerp(g, gradCoord(this.perm[var25 + 1], var7, var9 - 1.0D, var11 - 1.0D), gradCoord(this.perm[var29 + 1], var7 - 1.0D, var9 - 1.0D, var11 - 1.0D))));
    }

    private static double lerp(double x, double y, double t) {
        return y + x * (t - y);
    }

    private static double gradCoord(int i, double x, double y, double z) {
        int var12;
        double var8 = (var12 = i & 15) < 8 ? x : y;
        double var10 = var12 < 4 ? y : (var12 != 12 && var12 != 14 ? z : x);
        return ((var12 & 1) == 0 ? var8 : -var8) + ((var12 & 2) == 0 ? var10 : -var10);
    }

    public double sample(double x, double y) {
        return this.sample(x, y, 0.0D);
    }

    public double sampleD(double x, double y, double z) {
        return this.sample(x, y, z);
    }

    public void create(double[] map, int x, int y, int z, int width, int height, int depth, double d, double e, double f, double g) {
        int var16 = 0;
        double var17 = 1.0D / g;
        double var61 = -1;
        double var26 = 0.0D;
        double var28 = 0.0D;
        double var30 = 0.0D;
        double var32 = 0.0D;

        for(int var22 = 0; var22 < width; ++var22) {
            double var35;
            int var15 = (int)(var35 = (double)(x + var22) * d + this.offsetX);
            if (var35 < (double)var15) {
                --var15;
            }

            int var23 = var15 & 255;
            double var68;
            double var39 = (var68 = var35 - (double)var15) * var68 * var68 * (var68 * (var68 * 6.0D - 15.0D) + 10.0D);

            for(int var24 = 0; var24 < depth; ++var24) {
                double var42;
                var15 = (int)(var42 = (double)(z + var24) * f + this.offsetZ);
                if (var42 < (double)var15) {
                    --var15;
                }

                int var25 = var15 & 255;
                double var69;
                double var46 = (var69 = var42 - (double)var15) * var69 * var69 * (var69 * (var69 * 6.0D - 15.0D) + 10.0D);

                for(int var34 = 0; var34 < height; ++var34) {
                    double var49;
                    var15 = (int)(var49 = (double)(y + var34) * e + this.offsetY);
                    if (var49 < (double)var15) {
                        --var15;
                    }

                    int var20 = var15 & 255;
                    double var70;
                    double var53 = (var70 = var49 - (double)var15) * var70 * var70 * (var70 * (var70 * 6.0D - 15.0D) + 10.0D);
                    if (var34 == 0 || var20 != var61) {
                        var61 = var20;
                        var15 = this.perm[var23] + var20;
                        int var19 = this.perm[var15] + var25;
                        var15 = this.perm[var15 + 1] + var25;
                        var20 = this.perm[var23 + 1] + var20;
                        int var21 = this.perm[var20] + var25;
                        var20 = this.perm[var20 + 1] + var25;
                        var26 = lerp(var39, gradCoord(this.perm[var19], var68, var70, var69), gradCoord(this.perm[var21], var68 - 1.0D, var70, var69));
                        var28 = lerp(var39, gradCoord(this.perm[var15], var68, var70 - 1.0D, var69), gradCoord(this.perm[var20], var68 - 1.0D, var70 - 1.0D, var69));
                        var30 = lerp(var39, gradCoord(this.perm[var19 + 1], var68, var70, var69 - 1.0D), gradCoord(this.perm[var21 + 1], var68 - 1.0D, var70, var69 - 1.0D));
                        var32 = lerp(var39, gradCoord(this.perm[var15 + 1], var68, var70 - 1.0D, var69 - 1.0D), gradCoord(this.perm[var20 + 1], var68 - 1.0D, var70 - 1.0D, var69 - 1.0D));
                    }

                    double var55 = lerp(var53, var26, var28);
                    double var57 = lerp(var53, var30, var32);
                    double var59 = lerp(var46, var55, var57);
                    int var10001 = var16++;
                    map[var10001] += var59 * var17;
                }
            }
        }

    }
}