package com.itselix99.betterworldoptions.world.worldtypes.alpha112.util.math.noise;

import net.minecraft.util.math.noise.NoiseSampler;

import java.util.Random;

public class PerlinNoiseSamplerAlpha112 extends NoiseSampler {
    private int[] perm;
    public double offsetX;
    public double offsetY;
    public double offsetZ;

    public PerlinNoiseSamplerAlpha112() {
        this(new Random());
    }

    public PerlinNoiseSamplerAlpha112(Random random) {
        this.perm = new int[512];
        this.offsetX = random.nextDouble() * 256.0D;
        this.offsetY = random.nextDouble() * 256.0D;
        this.offsetZ = random.nextDouble() * 256.0D;

        int var2;
        for(var2 = 0; var2 < 256; this.perm[var2] = var2++) {
        }

        for(var2 = 0; var2 < 256; ++var2) {
            int var3 = random.nextInt(256 - var2) + var2;
            int var4 = this.perm[var2];
            this.perm[var2] = this.perm[var3];
            this.perm[var3] = var4;
            this.perm[var2 + 256] = this.perm[var2];
        }

    }

    public double sample(double x, double y, double z) {
        double var7 = x + this.offsetX;
        double var9 = y + this.offsetY;
        double var11 = z + this.offsetZ;
        int var13 = (int)var7;
        int var14 = (int)var9;
        int var15 = (int)var11;
        if(var7 < (double)var13) {
            --var13;
        }

        if(var9 < (double)var14) {
            --var14;
        }

        if(var11 < (double)var15) {
            --var15;
        }

        int var16 = var13 & 255;
        int var17 = var14 & 255;
        int var18 = var15 & 255;
        var7 -= (double)var13;
        var9 -= (double)var14;
        var11 -= (double)var15;
        double var19 = var7 * var7 * var7 * (var7 * (var7 * 6.0D - 15.0D) + 10.0D);
        double var21 = var9 * var9 * var9 * (var9 * (var9 * 6.0D - 15.0D) + 10.0D);
        double var23 = var11 * var11 * var11 * (var11 * (var11 * 6.0D - 15.0D) + 10.0D);
        int var25 = this.perm[var16] + var17;
        int var26 = this.perm[var25] + var18;
        int var27 = this.perm[var25 + 1] + var18;
        int var28 = this.perm[var16 + 1] + var17;
        int var29 = this.perm[var28] + var18;
        int var30 = this.perm[var28 + 1] + var18;
        return this.lerp(var23, this.lerp(var21, this.lerp(var19, this.gradCoord(this.perm[var26], var7, var9, var11), this.gradCoord(this.perm[var29], var7 - 1.0D, var9, var11)), this.lerp(var19, this.gradCoord(this.perm[var27], var7, var9 - 1.0D, var11), this.gradCoord(this.perm[var30], var7 - 1.0D, var9 - 1.0D, var11))), this.lerp(var21, this.lerp(var19, this.gradCoord(this.perm[var26 + 1], var7, var9, var11 - 1.0D), this.gradCoord(this.perm[var29 + 1], var7 - 1.0D, var9, var11 - 1.0D)), this.lerp(var19, this.gradCoord(this.perm[var27 + 1], var7, var9 - 1.0D, var11 - 1.0D), this.gradCoord(this.perm[var30 + 1], var7 - 1.0D, var9 - 1.0D, var11 - 1.0D))));
    }

    public double lerp(double x, double y, double t) {
        return y + x * (t - y);
    }

    public double gradCoord(int i, double x, double y, double z) {
        int var8 = i & 15;
        double var9 = var8 < 8 ? x : y;
        double var11 = var8 < 4 ? y : (var8 != 12 && var8 != 14 ? z : x);
        return ((var8 & 1) == 0 ? var9 : -var9) + ((var8 & 2) == 0 ? var11 : -var11);
    }

    public double sample(double var1, double var3) {
        return this.sample(var1, var3, 0.0D);
    }

    public void create(double[] map, double x, double y, double z, int width, int height, int depth, double d, double e, double f, double g) {
        int var19 = 0;
        double var20 = 1.0D / g;
        int var22 = -1;
        boolean var23 = false;
        boolean var24 = false;
        boolean var25 = false;
        boolean var26 = false;
        boolean var27 = false;
        boolean var28 = false;
        double var29 = 0.0D;
        double var31 = 0.0D;
        double var33 = 0.0D;
        double var35 = 0.0D;

        for(int var37 = 0; var37 < width; ++var37) {
            double var38 = (x + (double)var37) * d + this.offsetX;
            int var40 = (int)var38;
            if(var38 < (double)var40) {
                --var40;
            }

            int var41 = var40 & 255;
            var38 -= (double)var40;
            double var42 = var38 * var38 * var38 * (var38 * (var38 * 6.0D - 15.0D) + 10.0D);

            for(int var44 = 0; var44 < depth; ++var44) {
                double var45 = (z + (double)var44) * f + this.offsetZ;
                int var47 = (int)var45;
                if(var45 < (double)var47) {
                    --var47;
                }

                int var48 = var47 & 255;
                var45 -= (double)var47;
                double var49 = var45 * var45 * var45 * (var45 * (var45 * 6.0D - 15.0D) + 10.0D);

                for(int var51 = 0; var51 < height; ++var51) {
                    double var52 = (y + (double)var51) * e + this.offsetY;
                    int var54 = (int)var52;
                    if(var52 < (double)var54) {
                        --var54;
                    }

                    int var55 = var54 & 255;
                    var52 -= (double)var54;
                    double var56 = var52 * var52 * var52 * (var52 * (var52 * 6.0D - 15.0D) + 10.0D);
                    if(var51 == 0 || var55 != var22) {
                        var22 = var55;
                        int var64 = this.perm[var41] + var55;
                        int var65 = this.perm[var64] + var48;
                        int var66 = this.perm[var64 + 1] + var48;
                        int var67 = this.perm[var41 + 1] + var55;
                        int var68 = this.perm[var67] + var48;
                        int var69 = this.perm[var67 + 1] + var48;
                        var29 = this.lerp(var42, this.gradCoord(this.perm[var65], var38, var52, var45), this.gradCoord(this.perm[var68], var38 - 1.0D, var52, var45));
                        var31 = this.lerp(var42, this.gradCoord(this.perm[var66], var38, var52 - 1.0D, var45), this.gradCoord(this.perm[var69], var38 - 1.0D, var52 - 1.0D, var45));
                        var33 = this.lerp(var42, this.gradCoord(this.perm[var65 + 1], var38, var52, var45 - 1.0D), this.gradCoord(this.perm[var68 + 1], var38 - 1.0D, var52, var45 - 1.0D));
                        var35 = this.lerp(var42, this.gradCoord(this.perm[var66 + 1], var38, var52 - 1.0D, var45 - 1.0D), this.gradCoord(this.perm[var69 + 1], var38 - 1.0D, var52 - 1.0D, var45 - 1.0D));
                    }

                    double var58 = this.lerp(var56, var29, var31);
                    double var60 = this.lerp(var56, var33, var35);
                    double var62 = this.lerp(var49, var58, var60);
                    int var10001 = var19++;
                    map[var10001] += var62 * var20;
                }
            }
        }

    }
}