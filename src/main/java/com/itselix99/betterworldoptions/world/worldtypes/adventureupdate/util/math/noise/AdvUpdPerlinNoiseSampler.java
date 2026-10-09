package com.itselix99.betterworldoptions.world.worldtypes.adventureupdate.util.math.noise;

import net.minecraft.util.math.noise.NoiseSampler;

import java.util.Random;

public class AdvUpdPerlinNoiseSampler extends NoiseSampler {
    private final int[] perm;
    public double offsetX;
    public double offsetY;
    public double offsetZ;

    public AdvUpdPerlinNoiseSampler() {
        this(new Random());
    }

    public AdvUpdPerlinNoiseSampler(Random random) {
        this.perm = new int[512];
        this.offsetX = random.nextDouble() * (double)256.0F;
        this.offsetY = random.nextDouble() * (double)256.0F;
        this.offsetZ = random.nextDouble() * (double)256.0F;

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

    public double sample(double x, double y, double z) {
        double var7 = x + this.offsetX;
        double var9 = y + this.offsetY;
        double var11 = z + this.offsetZ;
        int var13 = (int)var7;
        int var14 = (int)var9;
        int var15 = (int)var11;
        if (var7 < (double)var13) {
            --var13;
        }

        if (var9 < (double)var14) {
            --var14;
        }

        if (var11 < (double)var15) {
            --var15;
        }

        int var16 = var13 & 255;
        int var17 = var14 & 255;
        int var18 = var15 & 255;
        var7 -= var13;
        var9 -= var14;
        var11 -= var15;
        double var19 = var7 * var7 * var7 * (var7 * (var7 * (double)6.0F - (double)15.0F) + (double)10.0F);
        double var21 = var9 * var9 * var9 * (var9 * (var9 * (double)6.0F - (double)15.0F) + (double)10.0F);
        double var23 = var11 * var11 * var11 * (var11 * (var11 * (double)6.0F - (double)15.0F) + (double)10.0F);
        int var25 = this.perm[var16] + var17;
        int var26 = this.perm[var25] + var18;
        int var27 = this.perm[var25 + 1] + var18;
        int var28 = this.perm[var16 + 1] + var17;
        int var29 = this.perm[var28] + var18;
        int var30 = this.perm[var28 + 1] + var18;
        return this.lerp(var23, this.lerp(var21, this.lerp(var19, this.gradCoord(this.perm[var26], var7, var9, var11), this.gradCoord(this.perm[var29], var7 - (double)1.0F, var9, var11)), this.lerp(var19, this.gradCoord(this.perm[var27], var7, var9 - (double)1.0F, var11), this.gradCoord(this.perm[var30], var7 - (double)1.0F, var9 - (double)1.0F, var11))), this.lerp(var21, this.lerp(var19, this.gradCoord(this.perm[var26 + 1], var7, var9, var11 - (double)1.0F), this.gradCoord(this.perm[var29 + 1], var7 - (double)1.0F, var9, var11 - (double)1.0F)), this.lerp(var19, this.gradCoord(this.perm[var27 + 1], var7, var9 - (double)1.0F, var11 - (double)1.0F), this.gradCoord(this.perm[var30 + 1], var7 - (double)1.0F, var9 - (double)1.0F, var11 - (double)1.0F))));
    }

    public final double lerp(double x, double y, double t) {
        return y + x * (t - y);
    }

    public final double gradCoord(int i, double x, double y) {
        int var6 = i & 15;
        double var7 = (double)(1 - ((var6 & 8) >> 3)) * x;
        double var9 = var6 < 4 ? (double)0.0F : (var6 != 12 && var6 != 14 ? y : x);
        return ((var6 & 1) == 0 ? var7 : -var7) + ((var6 & 2) == 0 ? var9 : -var9);
    }

    public final double gradCoord(int i, double x, double y, double z) {
        int var8 = i & 15;
        double var9 = var8 < 8 ? x : y;
        double var11 = var8 < 4 ? y : (var8 != 12 && var8 != 14 ? z : x);
        return ((var8 & 1) == 0 ? var9 : -var9) + ((var8 & 2) == 0 ? var11 : -var11);
    }

    public double sample(double x, double y) {
        return this.sample(x, y, 0.0F);
    }

    public void create(double[] map, double x, double y, double z, int width, int height, int depth, double d, double e, double f, double g) {
        if (height == 1) {
            int var64;
            int var66;
            int var21;
            int var69;
            double var72;
            double var76;
            int var80 = 0;
            double var82 = (double)1.0F / g;

            for(int var30 = 0; var30 < width; ++var30) {
                double var83 = x + (double)var30 * d + this.offsetX;
                int var85 = (int)var83;
                if (var83 < (double)var85) {
                    --var85;
                }

                int var34 = var85 & 255;
                var83 -= var85;
                double var86 = var83 * var83 * var83 * (var83 * (var83 * (double)6.0F - (double)15.0F) + (double)10.0F);

                for(int var87 = 0; var87 < depth; ++var87) {
                    double var89 = z + (double)var87 * f + this.offsetZ;
                    int var91 = (int)var89;
                    if (var89 < (double)var91) {
                        --var91;
                    }

                    int var92 = var91 & 255;
                    var89 -= var91;
                    double var93 = var89 * var89 * var89 * (var89 * (var89 * (double)6.0F - (double)15.0F) + (double)10.0F);
                    var64 = this.perm[var34];
                    var66 = this.perm[var64] + var92;
                    var21 = this.perm[var34 + 1];
                    var69 = this.perm[var21] + var92;
                    var72 = this.lerp(var86, this.gradCoord(this.perm[var66], var83, var89), this.gradCoord(this.perm[var69], var83 - (double)1.0F, 0.0F, var89));
                    var76 = this.lerp(var86, this.gradCoord(this.perm[var66 + 1], var83, 0.0F, var89 - (double)1.0F), this.gradCoord(this.perm[var69 + 1], var83 - (double)1.0F, 0.0F, var89 - (double)1.0F));
                    double var94 = this.lerp(var93, var72, var76);
                    int var97 = var80++;
                    map[var97] += var94 * var82;
                }
            }

        } else {
            int var19 = 0;
            double var20 = (double)1.0F / g;
            int var22 = -1;
            int var23;
            int var24;
            int var25;
            int var26;
            int var27;
            int var28;
            double var29 = 0.0F;
            double var31 = 0.0F;
            double var33 = 0.0F;
            double var35 = 0.0F;

            for(int var37 = 0; var37 < width; ++var37) {
                double var38 = x + (double)var37 * d + this.offsetX;
                int var40 = (int)var38;
                if (var38 < (double)var40) {
                    --var40;
                }

                int var41 = var40 & 255;
                var38 -= var40;
                double var42 = var38 * var38 * var38 * (var38 * (var38 * (double)6.0F - (double)15.0F) + (double)10.0F);

                for(int var44 = 0; var44 < depth; ++var44) {
                    double var45 = z + (double)var44 * f + this.offsetZ;
                    int var47 = (int)var45;
                    if (var45 < (double)var47) {
                        --var47;
                    }

                    int var48 = var47 & 255;
                    var45 -= var47;
                    double var49 = var45 * var45 * var45 * (var45 * (var45 * (double)6.0F - (double)15.0F) + (double)10.0F);

                    for(int var51 = 0; var51 < height; ++var51) {
                        double var52 = y + (double)var51 * e + this.offsetY;
                        int var54 = (int)var52;
                        if (var52 < (double)var54) {
                            --var54;
                        }

                        int var55 = var54 & 255;
                        var52 -= var54;
                        double var56 = var52 * var52 * var52 * (var52 * (var52 * (double)6.0F - (double)15.0F) + (double)10.0F);
                        if (var51 == 0 || var55 != var22) {
                            var22 = var55;
                            var23 = this.perm[var41] + var55;
                            var24 = this.perm[var23] + var48;
                            var25 = this.perm[var23 + 1] + var48;
                            var26 = this.perm[var41 + 1] + var55;
                            var27 = this.perm[var26] + var48;
                            var28 = this.perm[var26 + 1] + var48;
                            var29 = this.lerp(var42, this.gradCoord(this.perm[var24], var38, var52, var45), this.gradCoord(this.perm[var27], var38 - (double)1.0F, var52, var45));
                            var31 = this.lerp(var42, this.gradCoord(this.perm[var25], var38, var52 - (double)1.0F, var45), this.gradCoord(this.perm[var28], var38 - (double)1.0F, var52 - (double)1.0F, var45));
                            var33 = this.lerp(var42, this.gradCoord(this.perm[var24 + 1], var38, var52, var45 - (double)1.0F), this.gradCoord(this.perm[var27 + 1], var38 - (double)1.0F, var52, var45 - (double)1.0F));
                            var35 = this.lerp(var42, this.gradCoord(this.perm[var25 + 1], var38, var52 - (double)1.0F, var45 - (double)1.0F), this.gradCoord(this.perm[var28 + 1], var38 - (double)1.0F, var52 - (double)1.0F, var45 - (double)1.0F));
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
}