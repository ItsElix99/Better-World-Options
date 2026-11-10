package com.itselix99.betterworldoptions.util.noise;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.NoiseSampler;

import java.util.Random;

public class BWOPerlinNoiseSampler extends NoiseSampler {
    private final int[] perm;
    private final boolean useOffset;
    public double offsetX;
    public double offsetY;
    public double offsetZ;

    public BWOPerlinNoiseSampler() {
        this(new Random(), false);
    }

    public BWOPerlinNoiseSampler(Random random, boolean useOffset) {
        this.perm = new int[512];
        this.useOffset = useOffset;

        if (useOffset) {
            this.offsetX = random.nextDouble() * 256.0D;
            this.offsetY = random.nextDouble() * 256.0D;
            this.offsetZ = random.nextDouble() * 256.0D;
        }

        int var3;
        for(var3 = 0; var3 < 256; this.perm[var3] = var3++) {
        }

        for(var3 = 0; var3 < 256; ++var3) {
            int var4 = random.nextInt(256 - var3) + var3;
            int var5 = this.perm[var3];
            this.perm[var3] = this.perm[var4];
            this.perm[var4] = var5;
            this.perm[var3 + 256] = this.perm[var3];
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
        var7 -= var13;
        var9 -= var14;
        var11 -= var15;
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

    public double sampleAlternate(double x, double y, double z) {
        if (this.useOffset) {
            double var7 = x + this.offsetX;
            double var9 = y + this.offsetY;
            double var11 = z + this.offsetZ;
            int var22 = MathHelper.floor(var7) & 255;
            int var2 = MathHelper.floor(var9) & 255;
            int var23 = MathHelper.floor(var11) & 255;
            var7 -= MathHelper.floor(var7);
            var9 -= MathHelper.floor(var9);
            var11 -= MathHelper.floor(var11);
            double var16 = fade(var7);
            double var18 = fade(var9);
            double var20 = fade(var11);
            int var4 = this.perm[var22] + var2;
            int var24 = this.perm[var4] + var23;
            var4 = this.perm[var4 + 1] + var23;
            var22 = this.perm[var22 + 1] + var2;
            var2 = this.perm[var22] + var23;
            var22 = this.perm[var22 + 1] + var23;
            return this.lerp(var20, this.lerp(var18, this.lerp(var16, this.gradCoord(this.perm[var24], var7, var9, var11), this.gradCoord(this.perm[var2], var7 - 1.0D, var9, var11)), this.lerp(var16, this.gradCoord(this.perm[var4], var7, var9 - 1.0D, var11), this.gradCoord(this.perm[var22], var7 - 1.0D, var9 - 1.0D, var11))), this.lerp(var18, this.lerp(var16, this.gradCoord(this.perm[var24 + 1], var7, var9, var11 - 1.0D), this.gradCoord(this.perm[var2 + 1], var7 - 1.0D, var9, var11 - 1.0D)), this.lerp(var16, this.gradCoord(this.perm[var4 + 1], var7, var9 - 1.0D, var11 - 1.0D), this.gradCoord(this.perm[var22 + 1], var7 - 1.0D, var9 - 1.0D, var11 - 1.0D))));
        } else {
            double var10;
            double var8 = y;
            int var2 = MathHelper.floor(x) & 255;
            int var21 = MathHelper.floor(y) & 255;
            int var4 = MathHelper.floor(0.0D) & 255;
            double var6 = x - (double)MathHelper.floor(x);
            var8 -= MathHelper.floor(var8);
            var10 = 0.0D - (double)MathHelper.floor(0.0D);
            double var15 = fade(var6);
            double var17 = fade(var8);
            double var19 = fade(var10);
            int var5 = this.perm[var2] + var21;
            int var12 = this.perm[var5] + var4;
            var5 = this.perm[var5 + 1] + var4;
            var2 = this.perm[var2 + 1] + var21;
            var21 = this.perm[var2] + var4;
            var2 = this.perm[var2 + 1] + var4;
            return this.lerp(var19, this.lerp(var17, this.lerp(var15, this.gradCoord(this.perm[var12], var6, var8, var10), this.gradCoord(this.perm[var21], var6 - 1.0D, var8, var10)), this.lerp(var15, this.gradCoord(this.perm[var5], var6, var8 - 1.0D, var10), this.gradCoord(this.perm[var2], var6 - 1.0D, var8 - 1.0D, var10))), this.lerp(var17, this.lerp(var15, this.gradCoord(this.perm[var12 + 1], var6, var8, var10 - 1.0D), this.gradCoord(this.perm[var21 + 1], var6 - 1.0D, var8, var10 - 1.0D)), this.lerp(var15, this.gradCoord(this.perm[var5 + 1], var6, var8 - 1.0D, var10 - 1.0D), this.gradCoord(this.perm[var2 + 1], var6 - 1.0D, var8 - 1.0D, var10 - 1.0D))));
        }
    }

    public double lerp(double x, double y, double t) {
        return y + x * (t - y);
    }

    public double gradCoord(int i, double x, double y) {
        int var6 = i & 15;
        double var7 = (double)(1 - ((var6 & 8) >> 3)) * x;
        double var9 = var6 < 4 ? (double)0.0F : (var6 != 12 && var6 != 14 ? y : x);
        return ((var6 & 1) == 0 ? var7 : -var7) + ((var6 & 2) == 0 ? var9 : -var9);
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

    public double fade(double var0) {
        return var0 * var0 * var0 * (var0 * (var0 * 6.0D - 15.0D) + 10.0D);
    }

    public double sampleAlternate(double x, double y) {
        return this.sampleAlternate(x, y, 0.0D);
    }

    public void generateNoiseAdvUpd(double[] map, double x, double y, double z, int width, int height, int depth, double scaleX, double scaleY, double scaleZ, double noiseScale) {
        int var10001;
        int var19;
        int var22;
        double var31;
        double var35;
        int var37;
        double var38;
        int var40;
        int var41;
        double var42;
        int var75;
        if(height == 1) {
            double var70;
            double var73;
            var75 = 0;
            double var77 = 1.0D / noiseScale;

            for(int var30 = 0; var30 < width; ++var30) {
                var31 = x + (double)var30 * scaleX + this.offsetX;
                int var78 = (int)var31;
                if(var31 < (double)var78) {
                    --var78;
                }

                int var34 = var78 & 255;
                var31 -= var78;
                var35 = var31 * var31 * var31 * (var31 * (var31 * 6.0D - 15.0D) + 10.0D);

                for(var37 = 0; var37 < depth; ++var37) {
                    var38 = z + (double)var37 * scaleZ + this.offsetZ;
                    var40 = (int)var38;
                    if(var38 < (double)var40) {
                        --var40;
                    }

                    var41 = var40 & 255;
                    var38 -= var40;
                    var42 = var38 * var38 * var38 * (var38 * (var38 * 6.0D - 15.0D) + 10.0D);
                    var19 = this.perm[var34];
                    int var66 = this.perm[var19] + var41;
                    int var67 = this.perm[var34 + 1];
                    var22 = this.perm[var67] + var41;
                    var70 = this.lerp(var35, this.gradCoord(this.perm[var66], var31, var38), this.gradCoord(this.perm[var22], var31 - 1.0D, 0.0D, var38));
                    var73 = this.lerp(var35, this.gradCoord(this.perm[var66 + 1], var31, 0.0D, var38 - 1.0D), this.gradCoord(this.perm[var22 + 1], var31 - 1.0D, 0.0D, var38 - 1.0D));
                    double var79 = this.lerp(var42, var70, var73);
                    var10001 = var75++;
                    map[var10001] += var79 * var77;
                }
            }

        } else {
            var19 = 0;
            double var20 = 1.0D / noiseScale;
            var22 = -1;
            double var29 = 0.0D;
            var31 = 0.0D;
            double var33 = 0.0D;
            var35 = 0.0D;

            for(var37 = 0; var37 < width; ++var37) {
                var38 = x + (double)var37 * scaleX + this.offsetX;
                var40 = (int)var38;
                if(var38 < (double)var40) {
                    --var40;
                }

                var41 = var40 & 255;
                var38 -= var40;
                var42 = var38 * var38 * var38 * (var38 * (var38 * 6.0D - 15.0D) + 10.0D);

                for(int var44 = 0; var44 < depth; ++var44) {
                    double var45 = z + (double)var44 * scaleZ + this.offsetZ;
                    int var47 = (int)var45;
                    if(var45 < (double)var47) {
                        --var47;
                    }

                    int var48 = var47 & 255;
                    var45 -= var47;
                    double var49 = var45 * var45 * var45 * (var45 * (var45 * 6.0D - 15.0D) + 10.0D);

                    for(int var51 = 0; var51 < height; ++var51) {
                        double var52 = y + (double)var51 * scaleY + this.offsetY;
                        int var54 = (int)var52;
                        if(var52 < (double)var54) {
                            --var54;
                        }

                        int var55 = var54 & 255;
                        var52 -= var54;
                        double var56 = var52 * var52 * var52 * (var52 * (var52 * 6.0D - 15.0D) + 10.0D);
                        if(var51 == 0 || var55 != var22) {
                            var22 = var55;
                            int var69 = this.perm[var41] + var55;
                            int var71 = this.perm[var69] + var48;
                            int var72 = this.perm[var69 + 1] + var48;
                            int var74 = this.perm[var41 + 1] + var55;
                            var75 = this.perm[var74] + var48;
                            int var76 = this.perm[var74 + 1] + var48;
                            var29 = this.lerp(var42, this.gradCoord(this.perm[var71], var38, var52, var45), this.gradCoord(this.perm[var75], var38 - 1.0D, var52, var45));
                            var31 = this.lerp(var42, this.gradCoord(this.perm[var72], var38, var52 - 1.0D, var45), this.gradCoord(this.perm[var76], var38 - 1.0D, var52 - 1.0D, var45));
                            var33 = this.lerp(var42, this.gradCoord(this.perm[var71 + 1], var38, var52, var45 - 1.0D), this.gradCoord(this.perm[var75 + 1], var38 - 1.0D, var52, var45 - 1.0D));
                            var35 = this.lerp(var42, this.gradCoord(this.perm[var72 + 1], var38, var52 - 1.0D, var45 - 1.0D), this.gradCoord(this.perm[var76 + 1], var38 - 1.0D, var52 - 1.0D, var45 - 1.0D));
                        }

                        double var58 = this.lerp(var56, var29, var31);
                        double var60 = this.lerp(var56, var33, var35);
                        double var62 = this.lerp(var49, var58, var60);
                        var10001 = var19++;
                        map[var10001] += var62 * var20;
                    }
                }
            }

        }
    }

    public void generateNoise(double[] map, double x, double y, double z, int width, int height, int depth, double scaleX, double scaleY, double scaleZ, double noiseScale) {
        int var10001;
        int var19;
        int var22;
        double var31;
        double var35;
        int var37;
        double var38;
        int var40;
        int var41;
        double var42;
        int var75;
        if(height == 1) {
            double var70;
            double var73;
            var75 = 0;
            double var77 = 1.0D / noiseScale;

            for(int var30 = 0; var30 < width; ++var30) {
                var31 = (x + (double)var30) * scaleX + this.offsetX;
                int var78 = (int)var31;
                if(var31 < (double)var78) {
                    --var78;
                }

                int var34 = var78 & 255;
                var31 -= var78;
                var35 = var31 * var31 * var31 * (var31 * (var31 * 6.0D - 15.0D) + 10.0D);

                for(var37 = 0; var37 < depth; ++var37) {
                    var38 = (z + (double)var37) * scaleZ + this.offsetZ;
                    var40 = (int)var38;
                    if(var38 < (double)var40) {
                        --var40;
                    }

                    var41 = var40 & 255;
                    var38 -= var40;
                    var42 = var38 * var38 * var38 * (var38 * (var38 * 6.0D - 15.0D) + 10.0D);
                    var19 = this.perm[var34];
                    int var66 = this.perm[var19] + var41;
                    int var67 = this.perm[var34 + 1];
                    var22 = this.perm[var67] + var41;
                    var70 = this.lerp(var35, this.gradCoord(this.perm[var66], var31, var38), this.gradCoord(this.perm[var22], var31 - 1.0D, 0.0D, var38));
                    var73 = this.lerp(var35, this.gradCoord(this.perm[var66 + 1], var31, 0.0D, var38 - 1.0D), this.gradCoord(this.perm[var22 + 1], var31 - 1.0D, 0.0D, var38 - 1.0D));
                    double var79 = this.lerp(var42, var70, var73);
                    var10001 = var75++;
                    map[var10001] += var79 * var77;
                }
            }
        } else {
            var19 = 0;
            double var20 = 1.0D / noiseScale;
            var22 = -1;
            double var29 = 0.0D;
            var31 = 0.0D;
            double var33 = 0.0D;
            var35 = 0.0D;

            for(var37 = 0; var37 < width; ++var37) {
                var38 = (x + (double)var37) * scaleX + this.offsetX;
                var40 = (int)var38;
                if(var38 < (double)var40) {
                    --var40;
                }

                var41 = var40 & 255;
                var38 -= var40;
                var42 = var38 * var38 * var38 * (var38 * (var38 * 6.0D - 15.0D) + 10.0D);

                for(int var44 = 0; var44 < depth; ++var44) {
                    double var45 = (z + (double)var44) * scaleZ + this.offsetZ;
                    int var47 = (int)var45;
                    if(var45 < (double)var47) {
                        --var47;
                    }

                    int var48 = var47 & 255;
                    var45 -= var47;
                    double var49 = var45 * var45 * var45 * (var45 * (var45 * 6.0D - 15.0D) + 10.0D);

                    for(int var51 = 0; var51 < height; ++var51) {
                        double var52 = (y + (double)var51) * scaleY + this.offsetY;
                        int var54 = (int)var52;
                        if(var52 < (double)var54) {
                            --var54;
                        }

                        int var55 = var54 & 255;
                        var52 -= var54;
                        double var56 = var52 * var52 * var52 * (var52 * (var52 * 6.0D - 15.0D) + 10.0D);
                        if(var51 == 0 || var55 != var22) {
                            var22 = var55;
                            int var69 = this.perm[var41] + var55;
                            int var71 = this.perm[var69] + var48;
                            int var72 = this.perm[var69 + 1] + var48;
                            int var74 = this.perm[var41 + 1] + var55;
                            var75 = this.perm[var74] + var48;
                            int var76 = this.perm[var74 + 1] + var48;
                            var29 = this.lerp(var42, this.gradCoord(this.perm[var71], var38, var52, var45), this.gradCoord(this.perm[var75], var38 - 1.0D, var52, var45));
                            var31 = this.lerp(var42, this.gradCoord(this.perm[var72], var38, var52 - 1.0D, var45), this.gradCoord(this.perm[var76], var38 - 1.0D, var52 - 1.0D, var45));
                            var33 = this.lerp(var42, this.gradCoord(this.perm[var71 + 1], var38, var52, var45 - 1.0D), this.gradCoord(this.perm[var75 + 1], var38 - 1.0D, var52, var45 - 1.0D));
                            var35 = this.lerp(var42, this.gradCoord(this.perm[var72 + 1], var38, var52 - 1.0D, var45 - 1.0D), this.gradCoord(this.perm[var76 + 1], var38 - 1.0D, var52 - 1.0D, var45 - 1.0D));
                        }

                        double var58 = this.lerp(var56, var29, var31);
                        double var60 = this.lerp(var56, var33, var35);
                        double var62 = this.lerp(var49, var58, var60);
                        var10001 = var19++;
                        map[var10001] += var62 * var20;
                    }
                }
            }

        }
    }

    public void generateNoiseAlpha(double[] map, double x, double y, double z, int width, int height, int depth, double scaleX, double scaleY, double scaleZ, double noiseScale) {
        int var19 = 0;
        double var20 = 1.0D / noiseScale;
        int var22 = -1;
        double var23 = 0.0D;
        double var25 = 0.0D;
        double var27 = 0.0D;
        double var29 = 0.0D;

        for(int var31 = 0; var31 < width; ++var31) {
            double var32 = (x + (double)var31) * scaleX + this.offsetX;
            int var34 = (int)var32;
            if(var32 < (double)var34) {
                --var34;
            }

            int var35 = var34 & 255;
            var32 -= var34;
            double var36 = var32 * var32 * var32 * (var32 * (var32 * 6.0D - 15.0D) + 10.0D);

            for(int var38 = 0; var38 < depth; ++var38) {
                double var39 = (z + (double)var38) * scaleZ + this.offsetZ;
                int var41 = (int)var39;
                if(var39 < (double)var41) {
                    --var41;
                }

                int var42 = var41 & 255;
                var39 -= var41;
                double var43 = var39 * var39 * var39 * (var39 * (var39 * 6.0D - 15.0D) + 10.0D);

                for(int var45 = 0; var45 < height; ++var45) {
                    double var46 = (y + (double)var45) * scaleY + this.offsetY;
                    int var48 = (int)var46;
                    if(var46 < (double)var48) {
                        --var48;
                    }

                    int var49 = var48 & 255;
                    var46 -= var48;
                    double var50 = var46 * var46 * var46 * (var46 * (var46 * 6.0D - 15.0D) + 10.0D);
                    if(var45 == 0 || var49 != var22) {
                        var22 = var49;
                        int var58 = this.perm[var35] + var49;
                        int var59 = this.perm[var58] + var42;
                        int var60 = this.perm[var58 + 1] + var42;
                        int var61 = this.perm[var35 + 1] + var49;
                        int var62 = this.perm[var61] + var42;
                        int var63 = this.perm[var61 + 1] + var42;
                        var23 = this.lerp(var36, this.gradCoord(this.perm[var59], var32, var46, var39), this.gradCoord(this.perm[var62], var32 - 1.0D, var46, var39));
                        var25 = this.lerp(var36, this.gradCoord(this.perm[var60], var32, var46 - 1.0D, var39), this.gradCoord(this.perm[var63], var32 - 1.0D, var46 - 1.0D, var39));
                        var27 = this.lerp(var36, this.gradCoord(this.perm[var59 + 1], var32, var46, var39 - 1.0D), this.gradCoord(this.perm[var62 + 1], var32 - 1.0D, var46, var39 - 1.0D));
                        var29 = this.lerp(var36, this.gradCoord(this.perm[var60 + 1], var32, var46 - 1.0D, var39 - 1.0D), this.gradCoord(this.perm[var63 + 1], var32 - 1.0D, var46 - 1.0D, var39 - 1.0D));
                    }

                    double var52 = this.lerp(var50, var23, var25);
                    double var54 = this.lerp(var50, var27, var29);
                    double var56 = this.lerp(var43, var52, var54);
                    int var10001 = var19++;
                    map[var10001] += var56 * var20;
                }
            }
        }

    }

    public void generateNoiseInfdev(double[] map, int x, int y, int z, int width, int height, int depth, double scaleX, double scaleY, double scaleZ, double noiseScale) {
        int var16 = 0;
        double var17 = 1.0D / noiseScale;
        int var61 = -1;
        double var26 = 0.0D;
        double var28 = 0.0D;
        double var30 = 0.0D;
        double var32 = 0.0D;

        for(int var22 = 0; var22 < width; ++var22) {
            double var35 = (double)(x + var22) * scaleX + this.offsetX;
            int var15 = (int)var35;
            if(var35 < (double)var15) {
                --var15;
            }

            int var23 = var15 & 255;
            var35 -= var15;
            double var39 = var35 * var35 * var35 * (var35 * (var35 * 6.0D - 15.0D) + 10.0D);

            for(int var24 = 0; var24 < depth; ++var24) {
                double var42 = (double)(z + var24) * scaleZ + this.offsetZ;
                var15 = (int)var42;
                if(var42 < (double)var15) {
                    --var15;
                }

                int var25 = var15 & 255;
                var42 -= var15;
                double var46 = var42 * var42 * var42 * (var42 * (var42 * 6.0D - 15.0D) + 10.0D);

                for(int var34 = 0; var34 < height; ++var34) {
                    double var49 = (double)(y + var34) * scaleY + this.offsetY;
                    var15 = (int)var49;
                    if(var49 < (double)var15) {
                        --var15;
                    }

                    int var20 = var15 & 255;
                    var49 -= var15;
                    double var53 = var49 * var49 * var49 * (var49 * (var49 * 6.0D - 15.0D) + 10.0D);
                    if(var34 == 0 || var20 != var61) {
                        var61 = var20;
                        var15 = this.perm[var23] + var20;
                        int var19 = this.perm[var15] + var25;
                        var15 = this.perm[var15 + 1] + var25;
                        var20 += this.perm[var23 + 1];
                        int var21 = this.perm[var20] + var25;
                        var20 = this.perm[var20 + 1] + var25;
                        var26 = this.lerp(var39, this.gradCoord(this.perm[var19], var35, var49, var42), this.gradCoord(this.perm[var21], var35 - 1.0D, var49, var42));
                        var28 = this.lerp(var39, this.gradCoord(this.perm[var15], var35, var49 - 1.0D, var42), this.gradCoord(this.perm[var20], var35 - 1.0D, var49 - 1.0D, var42));
                        var30 = this.lerp(var39, this.gradCoord(this.perm[var19 + 1], var35, var49, var42 - 1.0D), this.gradCoord(this.perm[var21 + 1], var35 - 1.0D, var49, var42 - 1.0D));
                        var32 = this.lerp(var39, this.gradCoord(this.perm[var15 + 1], var35, var49 - 1.0D, var42 - 1.0D), this.gradCoord(this.perm[var20 + 1], var35 - 1.0D, var49 - 1.0D, var42 - 1.0D));
                    }

                    double var55 = this.lerp(var53, var26, var28);
                    double var57 = this.lerp(var53, var30, var32);
                    double var59 = this.lerp(var46, var55, var57);
                    int var10001 = var16++;
                    map[var10001] += var59 * var17;
                }
            }
        }

    }
}