package com.itselix99.betterworldoptions.world.worldtypes.alpha112.math.noise;

import net.minecraft.util.math.noise.NoiseSampler;

import java.util.Random;

public class OctavePerlinNoiseSamplerAlpha112 extends NoiseSampler {
    private PerlinNoiseSamplerAlpha112[] octaveSamplers;
    private int octaves;

    public OctavePerlinNoiseSamplerAlpha112(Random random, int i) {
        this.octaves = i;
        this.octaveSamplers = new PerlinNoiseSamplerAlpha112[i];

        for(int var3 = 0; var3 < i; ++var3) {
            this.octaveSamplers[var3] = new PerlinNoiseSamplerAlpha112(random);
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

    public double[] create(double[] map, double x, double y, double z, int width, int height, int depth, double d, double e, double f) {
        if(map == null) {
            map = new double[width * height * depth];
        } else {
            for(int var17 = 0; var17 < map.length; ++var17) {
                map[var17] = 0.0D;
            }
        }

        double var20 = 1.0D;

        for(int var19 = 0; var19 < this.octaves; ++var19) {
            this.octaveSamplers[var19].create(map, x, y, z, width, height, depth, d * var20, e * var20, f * var20, var20);
            var20 /= 2.0D;
        }

        return map;
    }
}
