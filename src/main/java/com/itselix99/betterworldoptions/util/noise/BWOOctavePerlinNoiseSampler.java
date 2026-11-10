package com.itselix99.betterworldoptions.util.noise;

import com.itselix99.betterworldoptions.util.MathUtil;
import net.minecraft.util.math.noise.NoiseSampler;

import java.util.Random;

public class BWOOctavePerlinNoiseSampler extends NoiseSampler {
    private final BWOPerlinNoiseSampler[] octaveSamplers;
    private final int octaves;

    public BWOOctavePerlinNoiseSampler(Random random, int octaves, boolean useOffset) {
        this.octaves = octaves;
        this.octaveSamplers = new BWOPerlinNoiseSampler[octaves];

        for(int var3 = 0; var3 < octaves; ++var3) {
            this.octaveSamplers[var3] = new BWOPerlinNoiseSampler(random, useOffset);
        }

    }

    public double sample(double x, double y) {
        double var5 = 0.0D;
        double var7 = 1.0D;

        for(int var9 = 0; var9 < this.octaves; ++var9) {
            var5 += this.octaveSamplers[var9].sample(x * var7, y * var7) / var7;
            var7 /= 2.0D;
        }

        return var5;
    }

    public double sample(double x, double y, double z) {
        double var7 = 0.0D;
        double var9 = 1.0D;

        for(int var11 = 0; var11 < this.octaves; ++var11) {
            var7 += this.octaveSamplers[var11].sample(x * var9, y * var9, z * var9) / var9;
            var9 /= 2.0D;
        }

        return var7;
    }

    public double sampleInverse(double x, double y) {
        double var5 = 0.0D;
        double var7 = 1.0D;

        for (int var9 = 0; var9 < this.octaves; ++var9) {
            var5 += this.octaveSamplers[var9].sampleAlternate(x / var7, y / var7) * var7;
            var7 *= 2.0D;
        }

        return var5;
    }

    public double sampleInverse(double x, double y, double z) {
        double var7 = 0.0D;
        double var9 = 1.0D;

        for(int var11 = 0; var11 < this.octaves; ++var11) {
            var7 += this.octaveSamplers[var11].sampleAlternate(x / var9, y / var9, z / var9) * var9;
            var9 *= 2.0D;
        }

        return var7;
    }

    public double[] generateNoiseAdvUpd(double[] map, int x, int y, int z, int width, int height, int depth, double scaleX, double scaleY, double scaleZ) {
        if (map == null) {
            map = new double[width * height * depth];
        } else {
            for(int var17 = 0; var17 < map.length; ++var17) {
                map[var17] = 0.0D;
            }
        }

        double var20 = 1.0D;

        for(int var22 = 0; var22 < this.octaves; ++var22) {
            double var23 = (double)x * var20 * scaleX;
            double var25 = (double)y * var20 * scaleY;
            double var27 = (double)z * var20 * scaleZ;
            long var29 = MathUtil.lfloor(var23);
            long var31 = MathUtil.lfloor(var27);
            var23 -= (double)var29;
            var27 -= (double)var31;
            var29 %= 16777216L;
            var31 %= 16777216L;
            var23 += (double)var29;
            var27 += (double)var31;
            this.octaveSamplers[var22].generateNoiseAdvUpd(map, var23, var25, var27, width, height, depth, scaleX * var20, scaleY * var20, scaleZ * var20, var20);
            var20 /= 2.0D;
        }

        return map;
    }

    public double[] generateNoise(double[] map, double x, double y, double z, int width, int height, int depth, double scaleX, double scaleY, double scaleZ) {
        if(map == null) {
            map = new double[width * height * depth];
        } else {
            for(int var17 = 0; var17 < map.length; ++var17) {
                map[var17] = 0.0D;
            }
        }

        double var20 = 1.0D;

        for(int var19 = 0; var19 < this.octaves; ++var19) {
            this.octaveSamplers[var19].generateNoise(map, x, y, z, width, height, depth, scaleX * var20, scaleY * var20, scaleZ * var20, var20);
            var20 /= 2.0D;
        }

        return map;
    }

    public double[] generateNoiseAlpha(double[] map, double x, double y, double z, int width, int height, int depth, double scaleX, double scaleY, double scaleZ) {
        if(map == null) {
            map = new double[width * height * depth];
        } else {
            for(int var17 = 0; var17 < map.length; ++var17) {
                map[var17] = 0.0D;
            }
        }

        double var20 = 1.0D;

        for(int var19 = 0; var19 < this.octaves; ++var19) {
            this.octaveSamplers[var19].generateNoiseAlpha(map, x, y, z, width, height, depth, scaleX * var20, scaleY * var20, scaleZ * var20, var20);
            var20 /= 2.0D;
        }

        return map;
    }

    public double[] generateNoiseInfdev(double[] map, int x, int y, int z, int width, int height, int depth, double scaleX, double scaleY, double scaleZ) {
        if(map == null) {
            map = new double[width * height * depth];
        } else {
            for(int var14 = 0; var14 < map.length; ++var14) {
                map[var14] = 0.0D;
            }
        }

        double var17 = 1.0D;

        for(int var16 = 0; var16 < this.octaves; ++var16) {
            this.octaveSamplers[var16].generateNoiseInfdev(map, x, y, z, width, height, depth, scaleX * var17, scaleY * var17, scaleZ * var17, var17);
            var17 /= 2.0D;
        }

        return map;
    }

    public double[] generateNoiseAdvUpd(double[] map, int x, int z, int width, int depth, double scaleX, double scaleZ, double scaleExponent) {
        return this.generateNoiseAdvUpd(map, x, 10, z, width, 1, depth, scaleX, 1.0D, scaleZ);
    }

    public double[] generateNoise(double[] map, int x, int z, int width, int depth, double scaleX, double scaleZ, double scaleExponent) {
        return this.generateNoise(map, x, 10.0D, z, width, 1, depth, scaleX, 1.0D, scaleZ);
    }
}